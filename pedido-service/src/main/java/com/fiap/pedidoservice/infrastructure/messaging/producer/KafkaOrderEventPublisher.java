package com.fiap.pedidoservice.infrastructure.messaging.producer;

import com.fiap.pedidoservice.application.gateway.OrderEventPublisherGateway;
import com.fiap.pedidoservice.domain.model.Order;
import com.fiap.pedidoservice.infrastructure.messaging.dto.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOrderEventPublisher implements OrderEventPublisherGateway {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String orderCreatedTopic;

    public KafkaOrderEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                                    @Value("${kafka.topic.pedido-criado}") String orderCreatedTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderCreatedTopic = orderCreatedTopic;
    }

    @Override
    public void publishOrderCreated(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getId(),
                order.getCustomerId(),
                order.getTotalAmount()
        );

        kafkaTemplate.send(orderCreatedTopic, String.valueOf(order.getId()), event);
    }
}