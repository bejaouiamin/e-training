// java
package com.etraining.service;

import com.etraining.CandidatRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CandidatClient {

    private static final Logger log = LoggerFactory.getLogger(CandidatClient.class);

    private final WebClient webClient;
    private final KeycloakRegistrationService keycloakRegistrationService;

    public CandidatClient(@Value("${services.candidat.url}") String candidatServiceUrl,
                          KeycloakRegistrationService keycloakRegistrationService) {
        this.webClient = WebClient.builder()
                .baseUrl(candidatServiceUrl)
                .build();
        this.keycloakRegistrationService = keycloakRegistrationService;
    }

    public Mono<String> createCandidat(CandidatRequest request) {
        return keycloakRegistrationService.getAdminToken()
                .flatMap(token ->
                        webClient.post()
                                .uri("/api/v1/candidats/add")
                                .header("Authorization", "Bearer " + token)
                                .bodyValue(request)
                                .exchangeToMono(resp ->
                                        resp.bodyToMono(String.class)
                                                .flatMap(body -> {
                                                    if (resp.statusCode().is2xxSuccessful()) {
                                                        log.debug("Candidat service response 2xx: {}", body);
                                                        return Mono.just(body);
                                                    } else {
                                                        log.error("Candidat service error {} -> {}", resp.statusCode(), body);
                                                        return Mono.error(new RuntimeException(
                                                                "Candidat service failed: " + resp.statusCode() + " - " + body));
                                                    }
                                                })
                                )
                );
    }
}
