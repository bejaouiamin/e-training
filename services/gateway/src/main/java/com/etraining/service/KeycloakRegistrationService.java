package com.etraining.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class KeycloakRegistrationService {

    private final String keycloakUrl;
    private final String realm;
    private final String tokenUrl;
    private final String clientId;
    private final String clientSecret;
    private final WebClient webClient;
    private String clientUuid; // Cache pour le UUID du client

    public KeycloakRegistrationService(
            @Value("${keycloak.admin.url}") String keycloakUrl,
            @Value("${keycloak.admin.realm}") String realm,
            @Value("${keycloak.admin.token-url}") String tokenUrl,
            @Value("${keycloak.admin.client-id}") String clientId,
            @Value("${keycloak.admin.client-secret}") String clientSecret
    ) {
        this.keycloakUrl = keycloakUrl;
        this.realm = realm;
        this.tokenUrl = tokenUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.webClient = WebClient.create();
    }

    public Mono<String> getAdminToken() {
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

    /**
     * Inscription utilisateur avec assignation de rôle client
     */
    /**
     * Inscription utilisateur avec assignation de rôle client
     * Returns the Keycloak user ID
     */
    public Mono<String> registerUser(String fullName, String firstname, String lastname,
                                     String email, String password, String roleName) {
        String username = (email != null && !email.isBlank())
                ? email.trim().toLowerCase()
                : fullName.toLowerCase().replaceAll("[^a-z0-9]", "");

        Map<String, Object> user = Map.of(
                "username", username,
                "firstName", firstname,
                "lastName", lastname,
                "email", email,
                "emailVerified", true,
                "enabled", true,
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", password,
                        "temporary", false
                ))
        );

        return getAdminToken()
                .flatMap(token -> createUser(token, user))
                .flatMap(userInfo -> assignClientRole(userInfo, roleName)
                        .thenReturn(userInfo.get("userId").toString()));  // Return Keycloak ID
    }

    /**
     * Étape 1 : Créer l'utilisateur dans Keycloak
     */
    private Mono<Map<String, String>> createUser(String token, Map<String, Object> user) {
        return webClient.post()
                .uri(keycloakUrl + "/admin/realms/{realm}/users", realm)
                .header("Authorization", "Bearer " + token)
                .bodyValue(user)
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful()) {
                        // Extraire l'ID utilisateur depuis l'en-tête Location
                        String location = resp.headers().asHttpHeaders().getFirst(HttpHeaders.LOCATION);
                        if (location == null) {
                            return Mono.error(new RuntimeException("Location header missing"));
                        }
                        // Extraire le dernier segment de l'URL comme ID
                        String[] parts = location.split("/");
                        String userId = parts[parts.length - 1];

                        return Mono.just(Map.of("token", token, "userId", userId));
                    } else {
                        return resp.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(
                                        new RuntimeException("Keycloak create user failed: " + resp.statusCode() + " - " + body)
                                ));
                    }
                });
    }

    /**
     * Étape 2 : Assigner un rôle client à l'utilisateur
     */
    private Mono<String> assignClientRole(Map<String, String> userInfo, String roleName) {
        String token = userInfo.get("token");
        String userId = userInfo.get("userId");

        return getClientUuid(token)
                .flatMap(clientUuid -> getClientRoleId(token, clientUuid, roleName))
                .flatMap(roleId -> {
                    Map<String, Object> roleBody = Map.of(
                            "id", roleId,
                            "name", roleName
                    );

                    return webClient.post()
                            .uri(keycloakUrl + "/admin/realms/{realm}/users/{userId}/role-mappings/clients/{clientUuid}",
                                    realm, userId, clientUuid)
                            .header("Authorization", "Bearer " + token)
                            .bodyValue(List.of(roleBody))
                            .retrieve()
                            .bodyToMono(String.class)
                            .thenReturn(userId);
                });
    }

    /**
     * Obtenir le UUID du client
     */
    private Mono<String> getClientUuid(String token) {
        if (this.clientUuid != null) {
            return Mono.just(this.clientUuid);
        }

        return webClient.get()
                .uri(keycloakUrl + "/admin/realms/{realm}/clients", realm)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(List.class)
                .map(clients -> {
                    for (Object client : clients) {
                        if (client instanceof Map) {
                            Map<?, ?> clientMap = (Map<?, ?>) client;
                            if (clientId.equals(clientMap.get("clientId"))) {
                                String uuid = (String) clientMap.get("id");
                                this.clientUuid = uuid; // Cache le résultat
                                return uuid;
                            }
                        }
                    }
                    throw new RuntimeException("Client not found: " + clientId);
                });
    }

    /**
     * Obtenir l'ID d'un rôle client
     */
    private Mono<String> getClientRoleId(String token, String clientUuid, String roleName) {
        return webClient.get()
                .uri(keycloakUrl + "/admin/realms/{realm}/clients/{clientUuid}/roles", realm, clientUuid)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(List.class)
                .map(roles -> {
                    for (Object role : roles) {
                        if (role instanceof Map) {
                            Map<?, ?> roleMap = (Map<?, ?>) role;
                            if (roleName.equals(roleMap.get("name"))) {
                                return (String) roleMap.get("id");
                            }
                        }
                    }
                    throw new RuntimeException("Role not found: " + roleName);
                });
    }
}
