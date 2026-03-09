package com.fiap.pedidoservice.domain.exception;

public class UnauthorizedOrderAccessException extends RuntimeException {
    public UnauthorizedOrderAccessException() {
        super("You do not have access to this order");
    }
}