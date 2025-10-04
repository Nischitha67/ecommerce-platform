package com.example.authservice.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;


import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    // Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    // Security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // allow auth endpoints (login, register, token)
                .requestMatchers("/auth/**").permitAll()
                    .requestMatchers(
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/auth/**"
                    ).permitAll()
                // block everything else by default
                    .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")

                    // All other requests need authentication
                    .anyRequest().authenticated()
            );

        return http.build();
    }
}