package com.etraining.controller;

import com.etraining.FormateurRequest;
import com.etraining.service.CandidatClient;
import com.etraining.service.FormateurClient;
import com.etraining.service.KeycloakRegistrationService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import com.etraining.CandidatRequest; // Import depuis common-dto
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final KeycloakRegistrationService registrationService;
    private final CandidatClient candidatClient;
    private final FormateurClient formateurClient;
    private final WebClient webClient;

    public AuthController(KeycloakRegistrationService registrationService, FormateurClient formateurClient,CandidatClient candidatClient) {
        this.registrationService = registrationService;
        this.candidatClient = candidatClient;
        this.formateurClient = formateurClient;
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8222")
                .build();
    }

    @GetMapping("/login")
    public Mono<Map<String, Object>> login(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        return Mono.just(Map.of(
                "username", jwt.getSubject(),
                "email", jwt.getClaimAsString("email"),
                "claims", jwt.getClaims()
        ));
    }

    @PostMapping("/candidat/register")
    public Mono<String> registerCandidat(@RequestBody @Valid CandidatRequest candidatRequest) {
        // create Keycloak user then persist candidate in candidat service DB using admin token
        return registrationService.registerUser(
                        candidatRequest.fullName(),
                        candidatRequest.email(),
                        candidatRequest.password(),
                        "CANDIDAT"
                )
                .then(candidatClient.createCandidat(candidatRequest));
    }

    @PostMapping("/formateur/register")
    public Mono<String> registerFormateur(@RequestBody @Valid FormateurRequest formateurRequest) {
        return registrationService.registerUser(
                        formateurRequest.nom(),
                        formateurRequest.email(),
                        formateurRequest.password(),
                        "FORMATEUR"
                )
                .then(formateurClient.createFormateur(formateurRequest));
    }
}
