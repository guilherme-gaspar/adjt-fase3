package com.fiap.pedidoservice.application.gateway;

import com.fiap.pedidoservice.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryGateway {
    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findByCustomerId(Long customerId);
}