package com.company.TradingPlatform.user_service.service;

import com.company.TradingPlatform.user_service.entitiy.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${jwt.expirationMs}") // Fetching expiration time from application.properties
    private long jwtExpirationMs;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token with user details.
     */
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())  // ✅ Store email as "sub"
                .claim("userId", user.getId())  // ✅ Store userId
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))  // ✅ Configurable expiration
                .signWith(getSecretKey()) // ✅ Sign with HMAC key
                .compact();
    }

    /**
     * Extracts email (username) from the JWT token.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())  // ✅ New method for jjwt 0.12.6
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();  // ✅ Correctly returns email
    }

    /**
     * Extracts userId from the JWT token.
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())  // ✅ Updated method for jjwt 0.12.6
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("userId", Long.class);  // ✅ Extract userId safely
    }

    /**
     * Validates JWT token.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())  // ✅ Updated validation for jjwt 0.12.6
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            System.err.println("Invalid JWT token: " + e.getMessage());  // ✅ Logging error
            return false;
        }
    }
}
