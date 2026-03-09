package com.fiap.pagamentoservice.infrastructure.scheduler;

import com.fiap.pagamentoservice.application.usecase.ReprocessPendingPaymentsUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PendingPaymentReprocessor {

    private final ReprocessPendingPaymentsUseCase reprocessPendingPaymentsUseCase;

    public PendingPaymentReprocessor(ReprocessPendingPaymentsUseCase reprocessPendingPaymentsUseCase) {
        this.reprocessPendingPaymentsUseCase = reprocessPendingPaymentsUseCase;
    }

    @Scheduled(fixedDelay = 10000)
    public void reprocess() {
        reprocessPendingPaymentsUseCase.execute();
    }
}