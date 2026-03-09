package com.fiap.pagamentoservice.infrastructure.config;

import com.fiap.pagamentoservice.application.gateway.ExternalPaymentGateway;
import com.fiap.pagamentoservice.application.gateway.PaymentEventPublisherGateway;
import com.fiap.pagamentoservice.application.gateway.PaymentRepositoryGateway;
import com.fiap.pagamentoservice.application.usecase.ProcessPaymentUseCase;
import com.fiap.pagamentoservice.application.usecase.ReprocessPendingPaymentsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ProcessPaymentUseCase processPaymentUseCase(PaymentRepositoryGateway paymentRepositoryGateway,
                                                       ExternalPaymentGateway externalPaymentGateway,
                                                       PaymentEventPublisherGateway paymentEventPublisherGateway) {
        return new ProcessPaymentUseCase(paymentRepositoryGateway, externalPaymentGateway, paymentEventPublisherGateway);
    }

    @Bean
    public ReprocessPendingPaymentsUseCase reprocessPendingPaymentsUseCase(PaymentRepositoryGateway paymentRepositoryGateway,
                                                                           ExternalPaymentGateway externalPaymentGateway,
                                                                           PaymentEventPublisherGateway paymentEventPublisherGateway) {
        return new ReprocessPendingPaymentsUseCase(paymentRepositoryGateway, externalPaymentGateway, paymentEventPublisherGateway);
    }
}