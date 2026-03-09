package com.fiap.pagamentoservice.infrastructure.external.dto;

import java.math.BigDecimal;

public record ProcpagRequest(
        BigDecimal valor,
        String pagamento_id,
        String cliente_id
) {
}