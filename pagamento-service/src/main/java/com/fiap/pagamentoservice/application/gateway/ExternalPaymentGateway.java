package com.fiap.pagamentoservice.application.gateway;

import com.fiap.pagamentoservice.application.dto.ExternalPaymentRequest;

public interface ExternalPaymentGateway {
    void process(ExternalPaymentRequest request);
}