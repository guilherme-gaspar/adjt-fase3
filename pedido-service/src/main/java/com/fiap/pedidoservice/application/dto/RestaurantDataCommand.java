package com.fiap.pedidoservice.application.dto;

public record RestaurantDataCommand(
        Long restaurantId,
        String restaurantName
) {
}