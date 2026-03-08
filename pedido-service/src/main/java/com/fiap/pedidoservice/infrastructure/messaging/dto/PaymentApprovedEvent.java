package com.fiap.pedidoservice.infrastructure.messaging.dto;

public record PaymentApprovedEvent(
        Long orderId
) {
}