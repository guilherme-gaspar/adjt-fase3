package com.fiap.authservice.infrastructure.controller.dto;

public record LoginResponse(
        String token,
        String type,
        Long userId,
        String email,
        String role
) {
}