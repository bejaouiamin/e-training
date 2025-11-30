package com.etraining.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                    .pathMatchers("/eureka/**", "/api/auth/**", "/login/**", "/oauth2/**").permitAll()
                    .pathMatchers("/api/v1/candidats/**").hasRole("CANDIDAT")
                    .pathMatchers("/api/v1/formateurs/**").hasRole("FORMATEUR")
                    .anyExchange()
                    .authenticated()
            )
              .oauth2Login(Customizer.withDefaults())
              .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
