package com.example.apigateway.security;

import com.example.apigateway.resources.ApiPaths;
import com.example.apigateway.resources.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.GET, ApiPaths.DEBUG_HEADERS).permitAll()

                .pathMatchers(HttpMethod.GET, ApiPaths.PASSENGERS).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.GET, ApiPaths.PASSENGERS_STATUS).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.GET, ApiPaths.PASSENGER_BY_ID).hasRole(Roles.PASSENGER)
                .pathMatchers(HttpMethod.GET, ApiPaths.PASSENGERS_IS_FREE).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.POST, ApiPaths.PASSENGERS).hasRole(Roles.PASSENGER)
                .pathMatchers(HttpMethod.PUT, ApiPaths.PASSENGERS + "/**").hasRole(Roles.PASSENGER)
                .pathMatchers(HttpMethod.DELETE, ApiPaths.PASSENGERS + "/**").hasRole(Roles.PASSENGER)

                .pathMatchers(HttpMethod.GET, ApiPaths.CARS).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.GET, ApiPaths.CARS_STATUS).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.GET, ApiPaths.CAR_BY_ID).hasRole(Roles.DRIVER)
                .pathMatchers(HttpMethod.POST, ApiPaths.CARS + "/**").hasRole(Roles.DRIVER)
                .pathMatchers(HttpMethod.PUT, ApiPaths.CARS + "/**").hasRole(Roles.DRIVER)
                .pathMatchers(HttpMethod.DELETE, ApiPaths.CARS + "/**").hasRole(Roles.DRIVER)

                .pathMatchers(HttpMethod.GET, ApiPaths.DRIVERS).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.GET, ApiPaths.DRIVERS_STATUS).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.GET, ApiPaths.DRIVER_BY_ID).hasRole(Roles.DRIVER)
                .pathMatchers(HttpMethod.POST, ApiPaths.DRIVERS).hasRole(Roles.DRIVER)
                .pathMatchers(HttpMethod.PUT, ApiPaths.DRIVERS + "/**").hasRole(Roles.DRIVER)
                .pathMatchers(HttpMethod.PUT, ApiPaths.DRIVERS_ASSIGN).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.PUT, ApiPaths.DRIVERS_RELEASE).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.DELETE, ApiPaths.DRIVERS + "/**").hasRole(Roles.DRIVER)

                .pathMatchers(HttpMethod.GET, ApiPaths.RIDES).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.GET, ApiPaths.RIDES_STATUS).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.GET, ApiPaths.RIDE_BY_ID).hasAnyRole(Roles.DRIVER, Roles.PASSENGER)
                .pathMatchers(HttpMethod.POST, ApiPaths.RIDES).hasRole(Roles.PASSENGER)
                .pathMatchers(HttpMethod.PUT, ApiPaths.RIDES + "/**").hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.PUT, ApiPaths.RIDES_CHANGE_STATUS).hasRole(Roles.ADMIN)

                .pathMatchers(HttpMethod.GET, ApiPaths.RATING).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.POST, ApiPaths.RATING).hasAnyRole(Roles.PASSENGER, Roles.DRIVER)
                .pathMatchers(HttpMethod.GET, ApiPaths.RATING_AVG_PASSENGER).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.GET, ApiPaths.RATING_AVG_DRIVER).hasRole(Roles.ADMIN)
                .pathMatchers(HttpMethod.DELETE, ApiPaths.RATING + "/**").hasAnyRole(Roles.PASSENGER, Roles.DRIVER)

                .pathMatchers(ApiPaths.API_PREFIX + "**").authenticated()
                .anyExchange().permitAll()
            )
            .oauth2ResourceServer(oAuth2 -> oAuth2
                .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())))
            .csrf(ServerHttpSecurity.CsrfSpec::disable);

        return httpSecurity.build();
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new AuthServerRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}