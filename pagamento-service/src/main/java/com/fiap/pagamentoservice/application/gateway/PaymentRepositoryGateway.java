package com.fiap.pagamentoservice.application.gateway;

import com.fiap.pagamentoservice.domain.model.Payment;
import com.fiap.pagamentoservice.domain.model.PaymentStatus;

import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryGateway {
    Payment save(Payment payment);

    Optional<Payment> findByOrderId(Long orderId);

    List<Payment> findByStatus(PaymentStatus status);
}