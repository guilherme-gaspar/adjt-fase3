package com.fiap.authservice.application.gateway;

import com.fiap.authservice.domain.model.User;

public interface TokenGateway {
    String generateToken(User user);
}