package com.etraining.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter delegate = new JwtGrantedAuthoritiesConverter();
    private final String clientId;

    public KeycloakJwtAuthenticationConverter(String clientId) {
        this.clientId = Objects.requireNonNull(clientId);
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        var merged = Stream.concat(
                Optional.ofNullable(delegate.convert(source)).orElseGet(Collections::emptySet).stream(),
                extractRoles(source).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(source, merged);
    }

    private Set<GrantedAuthority> extractRoles(Jwt jwt) {
        Set<GrantedAuthority> roles = new HashSet<>();

        Object realmAccess = jwt.getClaim("realm_access");
        if (realmAccess instanceof Map<?, ?> rmap) {
            Object r = rmap.get("roles");
            if (r instanceof Collection<?> rc) {
                rc.stream().map(Object::toString).map(this::toAuthority).forEach(roles::add);
            }
        }

        Object resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess instanceof Map<?, ?> resources) {
            Object client = resources.get(clientId);
            if (client instanceof Map<?, ?> cmap) {
                Object r = cmap.get("roles");
                if (r instanceof Collection<?> cc) {
                    cc.stream().map(Object::toString).map(this::toAuthority).forEach(roles::add);
                }
            }
        }

        return roles;
    }

    private SimpleGrantedAuthority toAuthority(String role) {
        return new SimpleGrantedAuthority("ROLE_" + role.replace("-", "_"));
    }
}
