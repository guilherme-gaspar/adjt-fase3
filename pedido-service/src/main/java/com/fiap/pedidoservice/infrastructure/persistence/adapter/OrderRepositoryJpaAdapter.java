package com.fiap.pedidoservice.infrastructure.persistence.adapter;

import com.fiap.pedidoservice.application.gateway.OrderRepositoryGateway;
import com.fiap.pedidoservice.domain.model.Order;
import com.fiap.pedidoservice.infrastructure.mapper.OrderEntityMapper;
import com.fiap.pedidoservice.infrastructure.persistence.repository.SpringDataOrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderRepositoryJpaAdapter implements OrderRepositoryGateway {

    private final SpringDataOrderRepository repository;
    private final OrderEntityMapper mapper;

    public OrderRepositoryJpaAdapter(SpringDataOrderRepository repository,
                                     OrderEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Order save(Order order) {
        return mapper.toDomain(repository.save(mapper.toEntity(order)));
    }

    @Override
    public Optional<Order> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}