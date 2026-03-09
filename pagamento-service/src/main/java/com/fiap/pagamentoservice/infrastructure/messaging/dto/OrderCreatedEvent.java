package com.fiap.pagamentoservice.infrastructure.messaging.dto;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        Long orderId,
        Long customerId,
        BigDecimal totalAmount
) {
}