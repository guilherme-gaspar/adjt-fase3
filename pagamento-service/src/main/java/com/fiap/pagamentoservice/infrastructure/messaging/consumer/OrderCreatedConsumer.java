package com.fiap.pagamentoservice.infrastructure.messaging.consumer;

import com.fiap.pagamentoservice.application.dto.OrderCreatedEventInput;
import com.fiap.pagamentoservice.application.usecase.ProcessPaymentUseCase;
import com.fiap.pagamentoservice.infrastructure.messaging.dto.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {

    private final ProcessPaymentUseCase processPaymentUseCase;

    public OrderCreatedConsumer(ProcessPaymentUseCase processPaymentUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
    }

    @KafkaListener(topics = "${kafka.topic.pedido-criado}", groupId = "${spring.kafka.consumer.group-id}")
    public void handle(OrderCreatedEvent event) {
        processPaymentUseCase.execute(
                new OrderCreatedEventInput(
                        event.orderId(),
                        event.customerId(),
                        event.totalAmount()
                )
        );
    }
}