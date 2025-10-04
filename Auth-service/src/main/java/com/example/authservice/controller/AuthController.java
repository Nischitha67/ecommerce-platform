package com.example.authservice.controller;

import com.example.authservice.dto.JwtResponse;
import com.example.authservice.dto.LoginRequest;
import com.example.authservice.dto.SignupRequest;
import com.example.authservice.dto.TokenRequest;
import com.example.authservice.model.Role;
import com.example.authservice.model.User;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired private AuthenticationManager authManager;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserDetailsService userDetailsService;

    // ---------------- Signup ----------------
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

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    // ---------------- Login ----------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.debug("Login request received for username: {}", request.getUsername());

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            log.debug("JWT generated for username {}: {}", request.getUsername(), token);

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            log.error("Login failed for username {}: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // ---------------- Validate Token ----------------
    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody TokenRequest tokenRequest) {
        log.debug("Received token validation request");

        String token = tokenRequest.getToken();
        String username = jwtUtil.extractUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        boolean valid = jwtUtil.validateToken(token, userDetails);

        log.debug("Token validation result: {}", valid);

        if (valid) {
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "username", username,
                    "roles", userDetails.getAuthorities().stream()
                            .map(a -> a.getAuthority())
                            .collect(Collectors.toList())
            ));
        } else {
            return ResponseEntity.ok(Map.of("valid", false));
        }
    }

    // ---------------- Validate Role ----------------
    @PostMapping("/validate-role")
    public Boolean validateRole(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"));
    }
}
