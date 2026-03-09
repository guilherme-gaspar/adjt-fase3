package com.fiap.pedidoservice.application.usecase;

import com.fiap.pedidoservice.application.dto.CreateOrderCommand;
import com.fiap.pedidoservice.application.dto.CreateOrderItemCommand;
import com.fiap.pedidoservice.application.dto.OrderItemOutput;
import com.fiap.pedidoservice.application.dto.OrderOutput;
import com.fiap.pedidoservice.application.gateway.OrderRepositoryGateway;
import com.fiap.pedidoservice.domain.model.Order;
import com.fiap.pedidoservice.domain.model.OrderItem;
import com.fiap.pedidoservice.domain.model.RestaurantData;

import java.util.List;

public class CreateOrderUseCase {

    private final OrderRepositoryGateway orderRepositoryGateway;

    public CreateOrderUseCase(OrderRepositoryGateway orderRepositoryGateway) {
        this.orderRepositoryGateway = orderRepositoryGateway;
    }

    public OrderOutput execute(CreateOrderCommand command) {
        RestaurantData restaurant = new RestaurantData(
                command.restaurant().restaurantId(),
                command.restaurant().restaurantName()
        );

        List<OrderItem> items = command.items().stream()
                .map(this::toOrderItem)
                .toList();

        Order order = Order.create(command.customerId(), restaurant, items);

        Order savedOrder = orderRepositoryGateway.save(order);

        return toOutput(savedOrder);
    }

    private OrderItem toOrderItem(CreateOrderItemCommand item) {
        return new OrderItem(
                item.productId(),
                item.name(),
                item.quantity(),
                item.price()
        );
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