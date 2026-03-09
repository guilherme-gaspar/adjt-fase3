package com.fiap.authservice.application.dto;

public record LoginCommand(
        String email,
        String password
) {
}