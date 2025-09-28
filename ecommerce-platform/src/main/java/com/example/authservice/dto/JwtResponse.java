package com.example.authservice.dto;

// dto/JwtResponse.java

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class JwtResponse {
    private String token;
}
