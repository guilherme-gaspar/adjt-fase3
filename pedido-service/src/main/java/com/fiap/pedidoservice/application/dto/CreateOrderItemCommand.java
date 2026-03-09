package com.fiap.pedidoservice.application.dto;

import java.math.BigDecimal;

public record CreateOrderItemCommand(
        Long productId,
        String name,
        Integer quantity,
        BigDecimal price
) {
}