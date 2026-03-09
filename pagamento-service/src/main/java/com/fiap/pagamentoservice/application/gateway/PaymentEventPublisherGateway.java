package com.fiap.pagamentoservice.application.gateway;

public interface PaymentEventPublisherGateway {
    void publishPaymentApproved(Long orderId);

    void publishPaymentPending(Long orderId);
}