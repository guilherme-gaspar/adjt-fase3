package com.fiap.pagamentoservice.infrastructure.messaging.dto;

public record PaymentPendingEvent(
        Long orderId
) {
}