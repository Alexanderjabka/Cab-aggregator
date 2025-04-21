package com.example.apigateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;

import reactor.core.publisher.Mono;
import org.springframework.core.convert.converter.Converter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final String API_PREFIX = "/api/v1/";

    @Bean
    SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeExchange(exchanges -> exchanges
                // Debug endpoint
                .pathMatchers(HttpMethod.GET, apiPath("drivers/debug-headers")).permitAll()

                // Passenger endpoints
                .pathMatchers(HttpMethod.GET, apiPath("passengers")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, apiPath("passengers/status/**")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, apiPath("passengers/{id}")).hasRole("PASSENGER")
                .pathMatchers(HttpMethod.GET, apiPath("passengers/isFree/**")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.POST, apiPath("passengers")).hasRole("PASSENGER")
                .pathMatchers(HttpMethod.PUT, apiPath("passengers/**")).hasRole("PASSENGER")
                .pathMatchers(HttpMethod.DELETE, apiPath("passengers/**")).hasRole("PASSENGER")

                // Car endpoints
                .pathMatchers(HttpMethod.GET, apiPath("cars")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, apiPath("cars/status/**")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, apiPath("cars/{id}")).hasRole("DRIVER")
                .pathMatchers(HttpMethod.POST, apiPath("cars/**")).hasRole("DRIVER")
                .pathMatchers(HttpMethod.PUT, apiPath("cars/**")).hasRole("DRIVER")
                .pathMatchers(HttpMethod.DELETE, apiPath("cars/**")).hasRole("DRIVER")

                // Driver endpoints
                .pathMatchers(HttpMethod.GET, apiPath("drivers")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, apiPath("drivers/status/**")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, apiPath("drivers/{id}")).hasRole("DRIVER")
                .pathMatchers(HttpMethod.POST, apiPath("drivers")).hasRole("DRIVER")
                .pathMatchers(HttpMethod.PUT, apiPath("drivers/**")).hasRole("DRIVER")
                .pathMatchers(HttpMethod.PUT, apiPath("drivers/assign")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, apiPath("drivers/release/**")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE, apiPath("drivers/**")).hasRole("DRIVER")

                // Ride endpoints
                .pathMatchers(HttpMethod.GET, apiPath("rides")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, apiPath("rides/status")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, apiPath("rides/{id}")).hasAnyRole("DRIVER", "PASSENGER")
                .pathMatchers(HttpMethod.POST, apiPath("rides")).hasRole("PASSENGER")
                .pathMatchers(HttpMethod.PUT, apiPath("rides/**")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, apiPath("rides/change-status/**")).hasRole("ADMIN")

                // Rating endpoints
                .pathMatchers(HttpMethod.GET, apiPath("rating")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.POST, apiPath("rating")).hasAnyRole("PASSENGER", "DRIVER")
                .pathMatchers(HttpMethod.GET, apiPath("rating/average/passenger-rating/**")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, apiPath("rating/average/driver-rating/**")).hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE, apiPath("rating/**")).hasAnyRole("PASSENGER", "DRIVER")

                // All other API endpoints require authentication
                .pathMatchers(apiPath("**")).authenticated()
                .anyExchange().permitAll()
            )
            .oauth2ResourceServer(oAuth2 -> oAuth2
                .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())))
            .csrf(ServerHttpSecurity.CsrfSpec::disable);

        return httpSecurity.build();
    }

    private String apiPath(String path) {
        return API_PREFIX + path;
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new AuthServerRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}