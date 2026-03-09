package com.fiap.pedidoservice.application.dto;

import java.util.List;

public record CreateOrderCommand(
        Long customerId,
        RestaurantDataCommand restaurant,
        List<CreateOrderItemCommand> items
) {
}