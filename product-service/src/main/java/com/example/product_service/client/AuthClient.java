package com.example.product_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", url = "http://localhost:8080/auth")
public interface AuthClient {

    @PostMapping("/validate-role")
    Boolean isAdmin(@RequestHeader("Authorization") String token);
}

