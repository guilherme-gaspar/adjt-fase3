package com.fiap.authservice.application.usecase;

import com.fiap.authservice.application.dto.RegisterUserCommand;
import com.fiap.authservice.application.dto.UserOutput;
import com.fiap.authservice.application.gateway.PasswordEncoderGateway;
import com.fiap.authservice.application.gateway.UserRepositoryGateway;
import com.fiap.authservice.domain.exception.UserAlreadyExistsException;
import com.fiap.authservice.domain.model.User;

public class RegisterUserUseCase {

    private final UserRepositoryGateway userRepositoryGateway;
    private final PasswordEncoderGateway passwordEncoderGateway;

    public RegisterUserUseCase(UserRepositoryGateway userRepositoryGateway,
                               PasswordEncoderGateway passwordEncoderGateway) {
        this.userRepositoryGateway = userRepositoryGateway;
        this.passwordEncoderGateway = passwordEncoderGateway;
    }

    public UserOutput execute(RegisterUserCommand command) {
        if (userRepositoryGateway.existsByEmail(command.email())) {
            throw new UserAlreadyExistsException(command.email());
        }

        User user = User.newClient(
                command.name(),
                command.email(),
                passwordEncoderGateway.encode(command.password())
        );

        User savedUser = userRepositoryGateway.save(user);

        return new UserOutput(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }
}