package com.fiap.pedidoservice.infrastructure.messaging.consumer;

import com.fiap.pedidoservice.application.usecase.MarkOrderAsPaidUseCase;
import com.fiap.pedidoservice.application.usecase.MarkOrderAsPendingPaymentUseCase;
import com.fiap.pedidoservice.infrastructure.messaging.dto.PaymentApprovedEvent;
import com.fiap.pedidoservice.infrastructure.messaging.dto.PaymentPendingEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventConsumer {

    private final MarkOrderAsPaidUseCase markOrderAsPaidUseCase;
    private final MarkOrderAsPendingPaymentUseCase markOrderAsPendingPaymentUseCase;

    public PaymentEventConsumer(MarkOrderAsPaidUseCase markOrderAsPaidUseCase,
                                MarkOrderAsPendingPaymentUseCase markOrderAsPendingPaymentUseCase) {
        this.markOrderAsPaidUseCase = markOrderAsPaidUseCase;
        this.markOrderAsPendingPaymentUseCase = markOrderAsPendingPaymentUseCase;
    }

    @KafkaListener(
            topics = "${kafka.topic.pagamento-aprovado}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "paymentApprovedKafkaListenerContainerFactory"
    )
    public void handlePaymentApproved(PaymentApprovedEvent event) {
        markOrderAsPaidUseCase.execute(event.orderId());
    }

    @KafkaListener(
            topics = "${kafka.topic.pagamento-pendente}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "paymentPendingKafkaListenerContainerFactory"
    )
    public void handlePaymentPending(PaymentPendingEvent event) {
        markOrderAsPendingPaymentUseCase.execute(event.orderId());
    }
}