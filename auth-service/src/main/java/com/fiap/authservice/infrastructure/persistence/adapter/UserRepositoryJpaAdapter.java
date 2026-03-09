package com.fiap.authservice.infrastructure.persistence.adapter;

import com.fiap.authservice.application.gateway.UserRepositoryGateway;
import com.fiap.authservice.domain.model.User;
import com.fiap.authservice.infrastructure.mapper.UserEntityMapper;
import com.fiap.authservice.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryJpaAdapter implements UserRepositoryGateway {

    private final SpringDataUserRepository repository;
    private final UserEntityMapper mapper;

    public UserRepositoryJpaAdapter(SpringDataUserRepository repository, UserEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        return mapper.toDomain(repository.save(mapper.toEntity(user)));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}