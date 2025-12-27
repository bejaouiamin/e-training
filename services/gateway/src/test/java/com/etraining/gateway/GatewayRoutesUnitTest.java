package com.etraining.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaAutoServiceRegistration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.config.import=optional:configserver:http://localhost:8888", // rend l'import optionnel
})
@AutoConfigureWebTestClient
class GatewayRoutesUnitTest {

    @Autowired
    private RouteLocator routeLocator;

    @MockBean
    private EurekaAutoServiceRegistration eurekaAutoServiceRegistration;

    @Test
    void routeLocator_shouldContainRoutes() {
        assertThat(routeLocator).isNotNull();

        List<Route> routes = routeLocator.getRoutes().collectList().block();
        assertThat(routes).isNotNull().isNotEmpty();
    }
}
