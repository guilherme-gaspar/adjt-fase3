package com.fiap.pedidoservice.infrastructure.config;

import com.fiap.pedidoservice.application.gateway.OrderEventPublisherGateway;
import com.fiap.pedidoservice.application.gateway.OrderRepositoryGateway;
import com.fiap.pedidoservice.application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepositoryGateway orderRepositoryGateway) {
        return new CreateOrderUseCase(orderRepositoryGateway);
    }

    @Bean
    public ConfirmOrderUseCase confirmOrderUseCase(OrderRepositoryGateway orderRepositoryGateway,
                                                   OrderEventPublisherGateway orderEventPublisherGateway) {
        return new ConfirmOrderUseCase(orderRepositoryGateway, orderEventPublisherGateway);
    }

    @Bean
    public GetOrderByIdUseCase getOrderByIdUseCase(OrderRepositoryGateway orderRepositoryGateway) {
        return new GetOrderByIdUseCase(orderRepositoryGateway);
    }

    @Bean
    public ListOrdersByCustomerUseCase listOrdersByCustomerUseCase(OrderRepositoryGateway orderRepositoryGateway) {
        return new ListOrdersByCustomerUseCase(orderRepositoryGateway);
    }

    @Bean
    public MarkOrderAsPaidUseCase markOrderAsPaidUseCase(OrderRepositoryGateway orderRepositoryGateway) {
        return new MarkOrderAsPaidUseCase(orderRepositoryGateway);
    }

    @Bean
    public MarkOrderAsPendingPaymentUseCase markOrderAsPendingPaymentUseCase(OrderRepositoryGateway orderRepositoryGateway) {
        return new MarkOrderAsPendingPaymentUseCase(orderRepositoryGateway);
    }
}