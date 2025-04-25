package com.example.apigateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtHeaderRelayFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return exchange.getPrincipal()
            .cast(Authentication.class)
            .filter(auth -> auth.getPrincipal() instanceof Jwt)
            .map(auth -> (Jwt) auth.getPrincipal())
            .flatMap(jwt -> {
                String userId = jwt.getSubject();
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .build();
                ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                return chain.filter(mutatedExchange);
            })
            .switchIfEmpty(chain.filter(exchange));
    }
}
