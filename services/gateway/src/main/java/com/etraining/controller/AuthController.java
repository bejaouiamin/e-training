package com.etraining.controller;

import com.etraining.service.KeycloakRegistrationService;
import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.etraining.CandidatRequest; // Import depuis common-dto
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final KeycloakRegistrationService registrationService;
    private final WebClient webClient;

    public AuthController(KeycloakRegistrationService registrationService) {
        this.registrationService = registrationService;
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8222")
                .build();
    }

    @GetMapping("/login")
    public Mono<Map<String, Object>> login(@AuthenticationPrincipal OidcUser user) {
        return Mono.just(Map.of(
                "username", user.getPreferredUsername(),
                "email", user.getEmail(),
                "roles", user.getAuthorities()
        ));
    }

    @PostMapping("/candidat/register")
    public Mono<String> registerCandidat(@RequestBody @Valid CandidatRequest candidatRequest) {
        return registrationService.registerUser(
                        candidatRequest.email(),
                        candidatRequest.email(),
                        candidatRequest.password(),
                        "CANDIDAT"
                )
                .then(webClient.post()
                        .uri("/api/v1/candidats")
                        .bodyValue(candidatRequest)
                        .retrieve()
                        .bodyToMono(String.class)
                );
    }

    @PostMapping("/formateur/register")
    public Mono<String> registerFormateur(@RequestBody Map<String, String> request) {
        return registrationService.registerUser(
                request.get("username"),
                request.get("email"),
                request.get("password"),
                "FORMATEUR"
        );
    }
}
