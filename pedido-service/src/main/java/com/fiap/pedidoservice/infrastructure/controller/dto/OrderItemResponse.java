package com.fiap.pedidoservice.infrastructure.controller.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        String name,
        Integer quantity,
        BigDecimal price,
        BigDecimal subtotal
) {
}