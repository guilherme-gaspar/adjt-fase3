package com.fiap.pagamentoservice.infrastructure.external.dto;

public record ProcpagRequest(
        Integer valor,
        String pagamento_id,
        String cliente_id
) {
}