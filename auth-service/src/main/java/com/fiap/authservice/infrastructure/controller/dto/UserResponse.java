package com.fiap.authservice.infrastructure.controller.dto;

public record UserResponse(
        Long id,
        String name,
        String email,
        String role
) {
}