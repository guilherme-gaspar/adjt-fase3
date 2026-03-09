package com.fiap.authservice.application.dto;

public record AuthOutput(
        String token,
        String type,
        Long userId,
        String email,
        String role
) {
}