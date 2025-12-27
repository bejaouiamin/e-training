// language: java
// file: services/gateway/src/test/java/com/training/gateway/GatewayIntegrationTest.java
package com.etraining.gateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.gateway.discovery.locator.enabled=false"
        })
class GatewayIntegrationTest {

    static WireMockServer wireMockServer;

    @LocalServerPort
    int port; // port du gateway

    WebTestClient webClient;

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(com.github.tomakehurst.wiremock.core.WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        if (wireMockServer != null) wireMockServer.stop();
    }

    @DynamicPropertySource
    static void registerGatewayRoute(DynamicPropertyRegistry registry) {
        // on crée dynamiquement une route qui pointe vers le WireMock
        registry.add("spring.cloud.gateway.routes[0].id", () -> "test-backend");
        registry.add("spring.cloud.gateway.routes[0].uri", () -> "http://localhost:" + wireMockServer.port());
        registry.add("spring.cloud.gateway.routes[0].predicates[0]", () -> "Path=/api/test/**");
    }

    @BeforeEach
    void setUp() {
        webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
        // stub du backend
        wireMockServer.stubFor(get(urlEqualTo("/api/test/hello"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"msg\":\"ok-from-backend\"}")));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.resetAll();
    }

    @Test
    void gateway_shouldForwardToBackend() {
        webClient.get()
                .uri("/api/test/hello")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody()
                .jsonPath("$.msg").isEqualTo("ok-from-backend");
    }
}
