package com.fiap.authservice.infrastructure.config;

import com.fiap.authservice.application.gateway.PasswordEncoderGateway;
import com.fiap.authservice.application.gateway.TokenGateway;
import com.fiap.authservice.application.gateway.UserRepositoryGateway;
import com.fiap.authservice.application.usecase.LoginUseCase;
import com.fiap.authservice.application.usecase.RegisterUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepositoryGateway userRepositoryGateway,
                                                   PasswordEncoderGateway passwordEncoderGateway) {
        return new RegisterUserUseCase(userRepositoryGateway, passwordEncoderGateway);
    }

    @Bean
    public LoginUseCase loginUseCase(UserRepositoryGateway userRepositoryGateway,
                                     PasswordEncoderGateway passwordEncoderGateway,
                                     TokenGateway tokenGateway) {
        return new LoginUseCase(userRepositoryGateway, passwordEncoderGateway, tokenGateway);
    }
}