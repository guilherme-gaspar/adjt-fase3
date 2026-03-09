package com.fiap.pagamentoservice.domain.exception;

public class ExternalPaymentException extends RuntimeException {
    public ExternalPaymentException(String message) {
        super(message);
    }
}