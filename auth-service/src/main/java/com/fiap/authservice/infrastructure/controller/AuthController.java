package com.fiap.authservice.infrastructure.controller;

import com.fiap.authservice.application.dto.AuthOutput;
import com.fiap.authservice.application.dto.UserOutput;
import com.fiap.authservice.application.usecase.LoginUseCase;
import com.fiap.authservice.application.usecase.RegisterUserUseCase;
import com.fiap.authservice.infrastructure.controller.dto.LoginRequest;
import com.fiap.authservice.infrastructure.controller.dto.LoginResponse;
import com.fiap.authservice.infrastructure.controller.dto.RegisterRequest;
import com.fiap.authservice.infrastructure.controller.dto.UserResponse;
import com.fiap.authservice.infrastructure.mapper.AuthControllerMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;
    private final AuthControllerMapper mapper;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUseCase loginUseCase,
                          AuthControllerMapper mapper) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterRequest request) {
        UserOutput output = registerUserUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(output));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthOutput output = loginUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.ok(mapper.toResponse(output));
    }
}