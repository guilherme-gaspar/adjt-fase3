package com.fiap.pagamentoservice.application.dto;

import java.math.BigDecimal;

public record OrderCreatedEventInput(
        Long orderId,
        Long customerId,
        BigDecimal totalAmount
) {
}