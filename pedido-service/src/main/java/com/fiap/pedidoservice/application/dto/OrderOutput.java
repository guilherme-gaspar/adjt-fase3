package com.fiap.pedidoservice.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderOutput(
        Long id,
        Long customerId,
        Long restaurantId,
        String restaurantName,
        List<OrderItemOutput> items,
        BigDecimal totalAmount,
        String status
) {
}