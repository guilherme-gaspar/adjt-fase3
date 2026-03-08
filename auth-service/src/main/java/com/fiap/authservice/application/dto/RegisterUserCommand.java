package com.fiap.authservice.application.dto;

public record RegisterUserCommand(
        String name,
        String email,
        String password
) {
}