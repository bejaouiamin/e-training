package com.etraining.service;

import com.etraining.FormateurRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FormateurClient {

    private static final Logger log = LoggerFactory.getLogger(FormateurClient.class);

    private final WebClient webClient;
    private final KeycloakRegistrationService keycloakRegistrationService;

    public FormateurClient(@Value("${services.formateur.url}") String formateurServiceUrl,
                           KeycloakRegistrationService keycloakRegistrationService) {
        this.webClient = WebClient.builder()
                .baseUrl(formateurServiceUrl)
                .build();
        this.keycloakRegistrationService = keycloakRegistrationService;
    }

    public Mono<String> createFormateur(FormateurRequest request) {
        return keycloakRegistrationService.getAdminToken()
                .flatMap(token ->
                        webClient.post()
                                .uri("/api/v1/formateurs/add") // adapter si votre controller utilise /add -> "/api/v1/formateurs/add"
                                .header("Authorization", "Bearer " + token)
                                .bodyValue(request)
                                .exchangeToMono(resp ->
                                        resp.bodyToMono(String.class)
                                                .flatMap(body -> {
                                                    if (resp.statusCode().is2xxSuccessful()) {
                                                        log.debug("Formateur service response 2xx: {}", body);
                                                        return Mono.just(body);
                                                    } else {
                                                        log.error("Formateur service error {} -> {}", resp.statusCode(), body);
                                                        return Mono.error(new RuntimeException(
                                                                "Formateur service failed: " + resp.statusCode() + " - " + body));
                                                    }
                                                })
                                )
                );
    }
}
