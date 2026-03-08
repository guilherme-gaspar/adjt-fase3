package com.fiap.authservice.application.dto;

public record UserOutput(
        Long id,
        String name,
        String email,
        String role
) {
}