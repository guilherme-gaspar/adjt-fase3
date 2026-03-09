package com.fiap.authservice.application.gateway;

import com.fiap.authservice.domain.model.User;

import java.util.Optional;

public interface UserRepositoryGateway {
    User save(User user);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}