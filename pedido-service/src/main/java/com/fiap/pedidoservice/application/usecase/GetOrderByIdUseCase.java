package com.fiap.pedidoservice.application.usecase;

import com.fiap.pedidoservice.application.dto.OrderItemOutput;
import com.fiap.pedidoservice.application.dto.OrderOutput;
import com.fiap.pedidoservice.application.gateway.OrderRepositoryGateway;
import com.fiap.pedidoservice.domain.exception.OrderNotFoundException;
import com.fiap.pedidoservice.domain.exception.UnauthorizedOrderAccessException;
import com.fiap.pedidoservice.domain.model.Order;

public class GetOrderByIdUseCase {

    private final OrderRepositoryGateway orderRepositoryGateway;

    public GetOrderByIdUseCase(OrderRepositoryGateway orderRepositoryGateway) {
        this.orderRepositoryGateway = orderRepositoryGateway;
    }

    public OrderOutput execute(Long orderId, Long customerId) {
        Order order = orderRepositoryGateway.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getCustomerId().equals(customerId)) {
            throw new UnauthorizedOrderAccessException();
        }

        return new OrderOutput(
                order.getId(),
                order.getCustomerId(),
                order.getRestaurantData().getRestaurantId(),
                order.getRestaurantData().getRestaurantName(),
                order.getItems().stream()
                        .map(item -> new OrderItemOutput(
                                item.getProductId(),
                                item.getName(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getSubtotal()
                        ))
                        .toList(),
                order.getTotalAmount(),
                order.getStatus().name()
        );
    }
}