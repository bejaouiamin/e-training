package com.etraining.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers("/eureka/**", "/api/auth/**", "/login/**", "/oauth2/**").permitAll()
                        .pathMatchers("/api/v1/candidats/**").permitAll()
                        .pathMatchers("/api/v1/formateurs/**").permitAll()
                        .pathMatchers("/api/v1/quizzes/**").permitAll()
                        .pathMatchers("/api/categories/**","/api/themes/**","/api/lessons/**").permitAll()
                        .anyExchange()
                        .authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer(auth ->
                        auth.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        new ReactiveJwtAuthenticationConverterAdapter(
                                                new KeycloakJwtAuthenticationConverter("micro-service-api")
                                        )
                                )
                        )
                );
        return http.build();
    }

}
