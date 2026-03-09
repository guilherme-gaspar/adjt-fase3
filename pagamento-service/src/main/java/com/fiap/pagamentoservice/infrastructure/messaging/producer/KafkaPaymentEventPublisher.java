package com.fiap.pagamentoservice.infrastructure.messaging.producer;

import com.fiap.pagamentoservice.application.gateway.PaymentEventPublisherGateway;
import com.fiap.pagamentoservice.infrastructure.messaging.dto.PaymentApprovedEvent;
import com.fiap.pagamentoservice.infrastructure.messaging.dto.PaymentPendingEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentEventPublisher implements PaymentEventPublisherGateway {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String approvedTopic;
    private final String pendingTopic;

    public KafkaPaymentEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                                      @Value("${kafka.topic.pagamento-aprovado}") String approvedTopic,
                                      @Value("${kafka.topic.pagamento-pendente}") String pendingTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.approvedTopic = approvedTopic;
        this.pendingTopic = pendingTopic;
    }

    @Override
    public void publishPaymentApproved(Long orderId) {
        kafkaTemplate.send(approvedTopic, String.valueOf(orderId), new PaymentApprovedEvent(orderId));
    }

    @Override
    public void publishPaymentPending(Long orderId) {
        kafkaTemplate.send(pendingTopic, String.valueOf(orderId), new PaymentPendingEvent(orderId));
    }
}