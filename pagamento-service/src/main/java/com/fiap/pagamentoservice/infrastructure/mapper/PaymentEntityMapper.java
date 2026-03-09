package com.fiap.pagamentoservice.infrastructure.mapper;

import com.fiap.pagamentoservice.domain.model.Payment;
import com.fiap.pagamentoservice.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentEntityMapper {

    public PaymentEntity toEntity(Payment payment) {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(payment.getId());
        entity.setOrderId(payment.getOrderId());
        entity.setCustomerId(payment.getCustomerId());
        entity.setExternalPaymentId(payment.getExternalPaymentId());
        entity.setAmount(payment.getAmount());
        entity.setStatus(payment.getStatus());
        return entity;
    }

    public Payment toDomain(PaymentEntity entity) {
        return new Payment(
                entity.getId(),
                entity.getOrderId(),
                entity.getCustomerId(),
                entity.getExternalPaymentId(),
                entity.getAmount(),
                entity.getStatus()
        );
    }
}