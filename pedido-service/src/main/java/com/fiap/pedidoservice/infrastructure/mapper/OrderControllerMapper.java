package com.fiap.pedidoservice.infrastructure.mapper;

import com.fiap.pedidoservice.application.dto.*;
import com.fiap.pedidoservice.infrastructure.controller.dto.CreateOrderRequest;
import com.fiap.pedidoservice.infrastructure.controller.dto.OrderItemResponse;
import com.fiap.pedidoservice.infrastructure.controller.dto.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderControllerMapper {

    public CreateOrderCommand toCommand(CreateOrderRequest request, Long customerId) {
        return new CreateOrderCommand(
                customerId,
                new RestaurantDataCommand(
                        request.restaurant().restaurantId(),
                        request.restaurant().restaurantName()
                ),
                request.items().stream()
                        .map(item -> new CreateOrderItemCommand(
                                item.productId(),
                                item.name(),
                                item.quantity(),
                                item.price()
                        ))
                        .toList()
        );
    }

    public OrderResponse toResponse(OrderOutput output) {
        return new OrderResponse(
                output.id(),
                output.customerId(),
                output.restaurantId(),
                output.restaurantName(),
                output.items().stream()
                        .map(this::toItemResponse)
                        .toList(),
                output.totalAmount(),
                output.status()
        );
    }

    private OrderItemResponse toItemResponse(OrderItemOutput item) {
        return new OrderItemResponse(
                item.productId(),
                item.name(),
                item.quantity(),
                item.price(),
                item.subtotal()
        );
    }
}