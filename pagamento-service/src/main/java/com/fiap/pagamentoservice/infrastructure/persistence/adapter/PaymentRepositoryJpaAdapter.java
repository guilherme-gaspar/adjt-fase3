package com.fiap.pagamentoservice.infrastructure.persistence.adapter;

import com.fiap.pagamentoservice.application.gateway.PaymentRepositoryGateway;
import com.fiap.pagamentoservice.domain.model.Payment;
import com.fiap.pagamentoservice.domain.model.PaymentStatus;
import com.fiap.pagamentoservice.infrastructure.mapper.PaymentEntityMapper;
import com.fiap.pagamentoservice.infrastructure.persistence.repository.SpringDataPaymentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentRepositoryJpaAdapter implements PaymentRepositoryGateway {

    private final SpringDataPaymentRepository repository;
    private final PaymentEntityMapper mapper;

    public PaymentRepositoryJpaAdapter(SpringDataPaymentRepository repository,
                                       PaymentEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Payment save(Payment payment) {
        return mapper.toDomain(repository.save(mapper.toEntity(payment)));
    }

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId).map(mapper::toDomain);
    }

    @Override
    public List<Payment> findByStatus(PaymentStatus status) {
        return repository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .toList();
    }
}