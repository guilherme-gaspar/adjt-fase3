package com.fiap.pagamentoservice.infrastructure.messaging.dto;

public record PaymentApprovedEvent(
        Long orderId
) {
}