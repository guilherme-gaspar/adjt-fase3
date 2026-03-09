package com.fiap.pedidoservice.infrastructure.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotNull @Valid RestaurantDataRequest restaurant,
        @NotEmpty List<@Valid CreateOrderItemRequest> items
) {
}