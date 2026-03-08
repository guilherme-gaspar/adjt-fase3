package com.fiap.pedidoservice.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtTokenParser {

    private final String secret;

    public JwtTokenParser(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public AuthenticatedUser parse(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long userId = ((Number) claims.get("userId")).longValue();
        String role = claims.get("role", String.class);
        String email = claims.getSubject();

        return new AuthenticatedUser(userId, email, role);
    }
}