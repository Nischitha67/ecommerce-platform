package com.example.api_gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class RoleBasedGatewayFilter implements GatewayFilter {
    // JWT secret
    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Your existing role-based logic here
        return chain.filter(exchange);
    }
}
