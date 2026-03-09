package com.fiap.pagamentoservice.application.dto;

import java.math.BigDecimal;

public record ExternalPaymentRequest(
        BigDecimal amount,
        String paymentId,
        String customerId
) {
}