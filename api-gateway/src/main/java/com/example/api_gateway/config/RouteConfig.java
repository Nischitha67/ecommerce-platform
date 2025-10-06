package com.example.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.api_gateway.filter.RoleBasedGatewayFilter;

@Configuration
public class RouteConfig {

    private final RoleBasedGatewayFilter roleBasedGatewayFilter;

    public RouteConfig(RoleBasedGatewayFilter roleBasedGatewayFilter) {
        this.roleBasedGatewayFilter = roleBasedGatewayFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth-service", r -> r.path("/auth/**")
                .uri("http://auth-service:8080"))
            .route("product-service", r -> r.path("/products/**")
                .filters(f -> f.filter(roleBasedGatewayFilter))
                .uri("http://product-service:8083"))
            .build();
    }
}
