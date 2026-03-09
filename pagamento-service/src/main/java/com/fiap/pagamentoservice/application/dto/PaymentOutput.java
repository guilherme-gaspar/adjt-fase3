package com.fiap.pagamentoservice.application.dto;

import java.math.BigDecimal;

public record PaymentOutput(
        Long orderId,
        Long customerId,
        String externalPaymentId,
        BigDecimal amount,
        String status
) {
}