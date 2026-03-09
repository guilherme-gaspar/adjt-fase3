package com.fiap.pedidoservice.infrastructure.controller.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        Long id,
        Long customerId,
        Long restaurantId,
        String restaurantName,
        List<OrderItemResponse> items,
        BigDecimal totalAmount,
        String status
) {
}