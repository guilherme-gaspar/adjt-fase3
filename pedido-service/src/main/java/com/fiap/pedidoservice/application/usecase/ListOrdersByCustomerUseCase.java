package com.fiap.pedidoservice.application.usecase;

import com.fiap.pedidoservice.application.dto.OrderItemOutput;
import com.fiap.pedidoservice.application.dto.OrderOutput;
import com.fiap.pedidoservice.application.gateway.OrderRepositoryGateway;
import com.fiap.pedidoservice.domain.model.Order;

import java.util.List;

public class ListOrdersByCustomerUseCase {

    private final OrderRepositoryGateway orderRepositoryGateway;

    public ListOrdersByCustomerUseCase(OrderRepositoryGateway orderRepositoryGateway) {
        this.orderRepositoryGateway = orderRepositoryGateway;
    }

    public List<OrderOutput> execute(Long customerId) {
        return orderRepositoryGateway.findByCustomerId(customerId).stream()
                .map(this::toOutput)
                .toList();
    }

    private OrderOutput toOutput(Order order) {
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