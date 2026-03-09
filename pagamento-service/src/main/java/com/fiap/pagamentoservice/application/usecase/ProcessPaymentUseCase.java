package com.fiap.pagamentoservice.application.usecase;

import com.fiap.pagamentoservice.application.dto.ExternalPaymentRequest;
import com.fiap.pagamentoservice.application.dto.OrderCreatedEventInput;
import com.fiap.pagamentoservice.application.gateway.ExternalPaymentGateway;
import com.fiap.pagamentoservice.application.gateway.PaymentEventPublisherGateway;
import com.fiap.pagamentoservice.application.gateway.PaymentRepositoryGateway;
import com.fiap.pagamentoservice.domain.model.Payment;

import java.util.UUID;

public class ProcessPaymentUseCase {

    private final PaymentRepositoryGateway paymentRepositoryGateway;
    private final ExternalPaymentGateway externalPaymentGateway;
    private final PaymentEventPublisherGateway paymentEventPublisherGateway;

    public ProcessPaymentUseCase(PaymentRepositoryGateway paymentRepositoryGateway,
                                 ExternalPaymentGateway externalPaymentGateway,
                                 PaymentEventPublisherGateway paymentEventPublisherGateway) {
        this.paymentRepositoryGateway = paymentRepositoryGateway;
        this.externalPaymentGateway = externalPaymentGateway;
        this.paymentEventPublisherGateway = paymentEventPublisherGateway;
    }

    public void execute(OrderCreatedEventInput input) {
        String paymentId = UUID.randomUUID().toString();

        Payment payment = Payment.newPending(
                input.orderId(),
                input.customerId(),
                paymentId,
                input.totalAmount()
        );

        Payment savedPayment = paymentRepositoryGateway.save(payment);

        try {
            externalPaymentGateway.process(
                    new ExternalPaymentRequest(
                            savedPayment.getAmount(),
                            savedPayment.getExternalPaymentId(),
                            String.valueOf(savedPayment.getCustomerId())
                    )
            );

            savedPayment.approve();
            paymentRepositoryGateway.save(savedPayment);
            paymentEventPublisherGateway.publishPaymentApproved(savedPayment.getOrderId());

        } catch (Exception ex) {
            savedPayment.markPending();
            paymentRepositoryGateway.save(savedPayment);
            paymentEventPublisherGateway.publishPaymentPending(savedPayment.getOrderId());
        }
    }
}