package com.fiap.authservice.infrastructure.mapper;

import com.fiap.authservice.application.dto.AuthOutput;
import com.fiap.authservice.application.dto.LoginCommand;
import com.fiap.authservice.application.dto.RegisterUserCommand;
import com.fiap.authservice.application.dto.UserOutput;
import com.fiap.authservice.infrastructure.controller.dto.LoginRequest;
import com.fiap.authservice.infrastructure.controller.dto.LoginResponse;
import com.fiap.authservice.infrastructure.controller.dto.RegisterRequest;
import com.fiap.authservice.infrastructure.controller.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthControllerMapper {

    public RegisterUserCommand toCommand(RegisterRequest request) {
        return new RegisterUserCommand(
                request.name(),
                request.email(),
                request.password()
        );
    }

    public LoginCommand toCommand(LoginRequest request) {
        return new LoginCommand(
                request.email(),
                request.password()
        );
    }

    public UserResponse toResponse(UserOutput output) {
        return new UserResponse(
                output.id(),
                output.name(),
                output.email(),
                output.role()
        );
    }

    public LoginResponse toResponse(AuthOutput output) {
        return new LoginResponse(
                output.token(),
                output.type(),
                output.userId(),
                output.email(),
                output.role()
        );
    }
}