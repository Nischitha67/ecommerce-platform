package com.example.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.dto.JwtResponse;
import com.example.authservice.dto.LoginRequest;
import com.example.authservice.dto.SignupRequest;
import com.example.authservice.dto.TokenRequest;
import com.example.authservice.dto.UserCreatedEvent;
import com.example.authservice.model.Role;
import com.example.authservice.model.User;
import com.example.authservice.security.jwt.JwtUtil;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// controller/AuthController.java
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired private AuthenticationManager authManager;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
   


    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username taken!");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.debug("Login request received for username: {}", request.getUsername());

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String username = authentication.getName();
            String token = jwtUtil.generateToken(username);

            log.debug("JWT generated for username {}: {}", username, token);

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            log.error("Login failed for username {}: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody TokenRequest tokenRequest) {
        log.debug("Received token validation request");

        boolean valid = jwtUtil.validateToken(tokenRequest.getToken());
        log.debug("Token validation result: {}", valid);

        if (valid) {
            String username = jwtUtil.extractUsername(tokenRequest.getToken());
            log.debug("Token belongs to username: {}", username);
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "username", username
            ));
        } else {
            log.debug("Token is invalid");
            return ResponseEntity.ok(Map.of("valid", false));
        }
    }


}
