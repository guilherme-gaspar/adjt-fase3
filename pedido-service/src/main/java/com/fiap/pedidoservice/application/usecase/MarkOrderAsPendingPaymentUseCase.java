package com.fiap.pedidoservice.application.usecase;

import com.fiap.pedidoservice.application.gateway.OrderRepositoryGateway;
import com.fiap.pedidoservice.domain.exception.OrderNotFoundException;
import com.fiap.pedidoservice.domain.model.Order;

public class MarkOrderAsPendingPaymentUseCase {

    private final OrderRepositoryGateway orderRepositoryGateway;

    public MarkOrderAsPendingPaymentUseCase(OrderRepositoryGateway orderRepositoryGateway) {
        this.orderRepositoryGateway = orderRepositoryGateway;
    }

    public void execute(Long orderId) {
        Order order = orderRepositoryGateway.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.markAsPendingPayment();
        orderRepositoryGateway.save(order);
    }
}