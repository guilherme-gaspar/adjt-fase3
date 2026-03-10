package com.fiap.pagamentoservice.infrastructure.external;

import com.fiap.pagamentoservice.application.dto.ExternalPaymentRequest;
import com.fiap.pagamentoservice.application.gateway.ExternalPaymentGateway;
import com.fiap.pagamentoservice.infrastructure.external.dto.ProcpagRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class ProcpagClient implements ExternalPaymentGateway {

    private final WebClient webClient;
    private final String baseUrl;

    public ProcpagClient(WebClient webClient,
                         @Value("${pagamento.externo.url}") String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    @Override
//    @Retry(name = "procpag", fallbackMethod = "fallback")
//    @CircuitBreaker(name = "procpag", fallbackMethod = "fallback")
    public void process(ExternalPaymentRequest request) {
        webClient.post()
                .uri(baseUrl + "/requisicao")
                .bodyValue(new ProcpagRequest(
                        request.amount().intValue(),
                        request.paymentId(),
                        request.customerId()
                ))
                .retrieve()
                .toBodilessEntity()
                .timeout(Duration.ofSeconds(3))
                .block();
    }

    public void fallback(ExternalPaymentRequest request, Throwable throwable) {
        throw new RuntimeException("External payment service unavailable", throwable);
    }
}