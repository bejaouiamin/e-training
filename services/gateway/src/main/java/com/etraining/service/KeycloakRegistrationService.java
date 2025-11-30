package com.etraining.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class KeycloakRegistrationService {

    private final String keycloakUrl;
    private final String tokenUrl;
    private final String clientId;
    private final String clientSecret;
    private final WebClient webClient;

    public KeycloakRegistrationService(
            @Value("${keycloak.admin.url}") String keycloakUrl,
            @Value("${keycloak.admin.token-url}") String tokenUrl,
            @Value("${keycloak.admin.client-id}") String clientId,
            @Value("${keycloak.admin.client-secret}") String clientSecret
    ) {
        this.keycloakUrl = keycloakUrl;
        this.tokenUrl = tokenUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.webClient = WebClient.create();
    }

    private Mono<String> getAdminToken() {
        String formData = String.format(
                "grant_type=client_credentials&client_id=%s&client_secret=%s",
                clientId, clientSecret
        );

        return webClient.post()
                .uri(tokenUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"));
    }

    public Mono<String> registerUser(String username, String email, String password, String role) {
        Map<String, Object> user = Map.of(
                "username", username,
                "email", email,
                "enabled", true,
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", password,
                        "temporary", false
                ))
        );

        return getAdminToken()
                .flatMap(token -> webClient.post()
                        .uri(keycloakUrl + "/admin/realms/micro-service/users")
                        .header("Authorization", "Bearer " + token)
                        .bodyValue(user)
                        .retrieve()
                        .bodyToMono(String.class)
                );
    }
}
