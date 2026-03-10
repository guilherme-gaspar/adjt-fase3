package com.fiap.pagamentoservice.application.usecase;

import com.fiap.pagamentoservice.application.dto.ExternalPaymentRequest;
import com.fiap.pagamentoservice.application.dto.OrderCreatedEventInput;
import com.fiap.pagamentoservice.application.gateway.ExternalPaymentGateway;
import com.fiap.pagamentoservice.application.gateway.PaymentEventPublisherGateway;
import com.fiap.pagamentoservice.application.gateway.PaymentRepositoryGateway;
import com.fiap.pagamentoservice.domain.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ProcessPaymentUseCase {

    private final PaymentRepositoryGateway paymentRepositoryGateway;
    private final ExternalPaymentGateway externalPaymentGateway;
    private final PaymentEventPublisherGateway paymentEventPublisherGateway;
    private final Logger logger = LoggerFactory.getLogger(ProcessPaymentUseCase.class);

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
            logger.info("Try to process payment: {}", paymentId);
            externalPaymentGateway.process(
                    new ExternalPaymentRequest(
                            savedPayment.getAmount(),
                            savedPayment.getExternalPaymentId(),
                            String.valueOf(savedPayment.getCustomerId())
                    )
            );
            logger.info("Process payment with success: {}", paymentId);
            savedPayment.approve();
            paymentRepositoryGateway.save(savedPayment);
            paymentEventPublisherGateway.publishPaymentApproved(savedPayment.getOrderId());

        } catch (Exception ex) {
            logger.info("Process payment with error: {}", paymentId);
            savedPayment.markPending();
            paymentRepositoryGateway.save(savedPayment);
            paymentEventPublisherGateway.publishPaymentPending(savedPayment.getOrderId());
        }
    }
}