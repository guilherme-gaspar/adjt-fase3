package com.fiap.pagamentoservice.domain.model;

import java.math.BigDecimal;

public class Payment {

    private Long id;
    private Long orderId;
    private Long customerId;
    private String externalPaymentId;
    private BigDecimal amount;
    private PaymentStatus status;

    public Payment(Long id,
                   Long orderId,
                   Long customerId,
                   String externalPaymentId,
                   BigDecimal amount,
                   PaymentStatus status) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.externalPaymentId = externalPaymentId;
        this.amount = amount;
        this.status = status;
    }

    public static Payment newPending(Long orderId, Long customerId, String externalPaymentId, BigDecimal amount) {
        return new Payment(null, orderId, customerId, externalPaymentId, amount, PaymentStatus.PENDENTE);
    }

    public void approve() {
        this.status = PaymentStatus.APROVADO;
    }

    public void markPending() {
        this.status = PaymentStatus.PENDENTE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getExternalPaymentId() {
        return externalPaymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}