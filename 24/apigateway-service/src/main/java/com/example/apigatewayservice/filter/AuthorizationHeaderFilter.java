package com.example.apigatewayservice.filter;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {

    private final String tokenSecret;

    public AuthorizationHeaderFilter(@Value("${token.secret}") String tokenSecret) {
        super(NameConfig.class);
        this.tokenSecret = tokenSecret;
    }

    // login -> token -> users (with token) -> header(include token)
    @Override
    public GatewayFilter apply(NameConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authHeader.replace("Bearer", "");

            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;

        try {
            subject = Jwts.parser().setSigningKey(tokenSecret)
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            returnValue = false;
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }

    // Mono, Flux -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String errMessage, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(errMessage);
        return response.setComplete();
    }
}
