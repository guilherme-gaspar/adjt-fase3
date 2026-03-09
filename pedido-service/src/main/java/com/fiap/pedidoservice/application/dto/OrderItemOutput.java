package com.fiap.pedidoservice.application.dto;

import java.math.BigDecimal;

public record OrderItemOutput(
        Long productId,
        String name,
        Integer quantity,
        BigDecimal price,
        BigDecimal subtotal
) {
}