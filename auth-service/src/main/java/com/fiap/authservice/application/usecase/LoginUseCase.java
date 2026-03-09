package com.fiap.authservice.application.usecase;

import com.fiap.authservice.application.dto.AuthOutput;
import com.fiap.authservice.application.dto.LoginCommand;
import com.fiap.authservice.application.gateway.PasswordEncoderGateway;
import com.fiap.authservice.application.gateway.TokenGateway;
import com.fiap.authservice.application.gateway.UserRepositoryGateway;
import com.fiap.authservice.domain.exception.InvalidCredentialsException;
import com.fiap.authservice.domain.model.User;

public class LoginUseCase {

    private final UserRepositoryGateway userRepositoryGateway;
    private final PasswordEncoderGateway passwordEncoderGateway;
    private final TokenGateway tokenGateway;

    public LoginUseCase(UserRepositoryGateway userRepositoryGateway,
                        PasswordEncoderGateway passwordEncoderGateway,
                        TokenGateway tokenGateway) {
        this.userRepositoryGateway = userRepositoryGateway;
        this.passwordEncoderGateway = passwordEncoderGateway;
        this.tokenGateway = tokenGateway;
    }

    public AuthOutput execute(LoginCommand command) {
        User user = userRepositoryGateway.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);

        boolean validPassword = passwordEncoderGateway.matches(command.password(), user.getPassword());

        if (!validPassword) {
            throw new InvalidCredentialsException();
        }

        String token = tokenGateway.generateToken(user);

        return new AuthOutput(
                token,
                "Bearer",
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}