package com.fiap.pagamentoservice.infrastructure.persistence.repository;

import com.fiap.pagamentoservice.domain.model.PaymentStatus;
import com.fiap.pagamentoservice.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataPaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByOrderId(Long orderId);

    List<PaymentEntity> findByStatus(PaymentStatus status);
}