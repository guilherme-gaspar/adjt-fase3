package com.fiap.pedidoservice.infrastructure.security;

public record AuthenticatedUser(
        Long userId,
        String email,
        String role
) {
}