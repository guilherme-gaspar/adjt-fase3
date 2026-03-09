package com.fiap.pedidoservice.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RestaurantDataRequest(
        @NotNull Long restaurantId,
        @NotBlank String restaurantName
) {
}