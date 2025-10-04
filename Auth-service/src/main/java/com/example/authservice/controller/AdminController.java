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

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired private AuthenticationManager authManager;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    // Only accessible by admins
    @PostMapping("/create-admin")
    public ResponseEntity<?> register(@RequestBody SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username taken!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);

        // Save user to the database
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }
}
