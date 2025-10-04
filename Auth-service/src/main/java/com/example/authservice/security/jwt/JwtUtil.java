package com.example.authservice.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // Base64 encoded 32+ byte secret key (strong enough for HS256)
    private static final String SECRET_KEY = "bXlTdXBlclNlY3JldEtleVRoYXRJc0F0TGVhc3QzMkNoYXJzTG9uZyEh";

    // Generate a SecretKey instance from the Base64 string
    private final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    // Generate JWT token for a user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Add roles to claims
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());
        claims.put("roles", roles);

        return createToken(claims, userDetails.getUsername());
    }

    // Create the JWT token
    private String createToken(Map<String, Object> claims, String subject) {
        long expirationTime = 1000 * 60 * 60 * 10; // 10 hours

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256) // ✅ secure signing
                .compact();
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract roles from token
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return (List<String>) claims.get("roles");
    }

    // Extract any claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // ✅ updated parser
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validate token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
