package com.fiap.pedidoservice.application.usecase;

import com.fiap.pedidoservice.application.dto.OrderItemOutput;
import com.fiap.pedidoservice.application.dto.OrderOutput;
import com.fiap.pedidoservice.application.gateway.OrderEventPublisherGateway;
import com.fiap.pedidoservice.application.gateway.OrderRepositoryGateway;
import com.fiap.pedidoservice.domain.exception.OrderNotFoundException;
import com.fiap.pedidoservice.domain.exception.UnauthorizedOrderAccessException;
import com.fiap.pedidoservice.domain.model.Order;

public class ConfirmOrderUseCase {

    private final OrderRepositoryGateway orderRepositoryGateway;
    private final OrderEventPublisherGateway orderEventPublisherGateway;

    public ConfirmOrderUseCase(OrderRepositoryGateway orderRepositoryGateway,
                               OrderEventPublisherGateway orderEventPublisherGateway) {
        this.orderRepositoryGateway = orderRepositoryGateway;
        this.orderEventPublisherGateway = orderEventPublisherGateway;
    }

    public OrderOutput execute(Long orderId, Long customerId) {
        Order order = orderRepositoryGateway.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getCustomerId().equals(customerId)) {
            throw new UnauthorizedOrderAccessException();
        }

        order.confirm();

        Order savedOrder = orderRepositoryGateway.save(order);

        orderEventPublisherGateway.publishOrderCreated(savedOrder);

        return new OrderOutput(
                savedOrder.getId(),
                savedOrder.getCustomerId(),
                savedOrder.getRestaurantData().getRestaurantId(),
                savedOrder.getRestaurantData().getRestaurantName(),
                savedOrder.getItems().stream()
                        .map(item -> new OrderItemOutput(
                                item.getProductId(),
                                item.getName(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getSubtotal()
                        ))
                        .toList(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus().name()
        );
    }
}