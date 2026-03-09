package com.fiap.pedidoservice.application.gateway;

import com.fiap.pedidoservice.domain.model.Order;

public interface OrderEventPublisherGateway {
    void publishOrderCreated(Order order);
}