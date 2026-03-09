package com.fiap.pagamentoservice.application.usecase;

import com.fiap.pagamentoservice.application.dto.ExternalPaymentRequest;
import com.fiap.pagamentoservice.application.gateway.ExternalPaymentGateway;
import com.fiap.pagamentoservice.application.gateway.PaymentEventPublisherGateway;
import com.fiap.pagamentoservice.application.gateway.PaymentRepositoryGateway;
import com.fiap.pagamentoservice.domain.model.Payment;
import com.fiap.pagamentoservice.domain.model.PaymentStatus;

import java.util.List;

public class ReprocessPendingPaymentsUseCase {

    private final PaymentRepositoryGateway paymentRepositoryGateway;
    private final ExternalPaymentGateway externalPaymentGateway;
    private final PaymentEventPublisherGateway paymentEventPublisherGateway;

    public ReprocessPendingPaymentsUseCase(PaymentRepositoryGateway paymentRepositoryGateway,
                                           ExternalPaymentGateway externalPaymentGateway,
                                           PaymentEventPublisherGateway paymentEventPublisherGateway) {
        this.paymentRepositoryGateway = paymentRepositoryGateway;
        this.externalPaymentGateway = externalPaymentGateway;
        this.paymentEventPublisherGateway = paymentEventPublisherGateway;
    }

    public void execute() {
        List<Payment> pendingPayments = paymentRepositoryGateway.findByStatus(PaymentStatus.PENDENTE);

        for (Payment payment : pendingPayments) {
            try {
                externalPaymentGateway.process(
                        new ExternalPaymentRequest(
                                payment.getAmount(),
                                payment.getExternalPaymentId(),
                                String.valueOf(payment.getCustomerId())
                        )
                );

                payment.approve();
                paymentRepositoryGateway.save(payment);
                paymentEventPublisherGateway.publishPaymentApproved(payment.getOrderId());

            } catch (Exception ignored) {
            }
        }
    }
}