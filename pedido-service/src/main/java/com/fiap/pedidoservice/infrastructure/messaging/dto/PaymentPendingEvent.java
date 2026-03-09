package com.fiap.pedidoservice.infrastructure.messaging.dto;

public record PaymentPendingEvent(
        Long orderId
) {
}