package com.fiap.pedidoservice.application.usecase;

import com.fiap.pedidoservice.application.gateway.OrderRepositoryGateway;
import com.fiap.pedidoservice.domain.exception.OrderNotFoundException;
import com.fiap.pedidoservice.domain.model.Order;
import org.springframework.transaction.annotation.Transactional;

public class MarkOrderAsPaidUseCase {

    private final OrderRepositoryGateway orderRepositoryGateway;

    public MarkOrderAsPaidUseCase(OrderRepositoryGateway orderRepositoryGateway) {
        this.orderRepositoryGateway = orderRepositoryGateway;
    }

    @Transactional
    public void execute(Long orderId) {
        Order order = orderRepositoryGateway.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.markAsPaid();
        orderRepositoryGateway.save(order);
    }
}