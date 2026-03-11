package com.fiap.pagamentoservice.infrastructure.external;

import com.fiap.pagamentoservice.application.dto.ExternalPaymentRequest;
import com.fiap.pagamentoservice.application.gateway.ExternalPaymentGateway;
import com.fiap.pagamentoservice.infrastructure.external.dto.ProcpagRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;


@Component
public class ProcpagClient implements ExternalPaymentGateway {

    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(ProcpagClient.class);

    public ProcpagClient(
            WebClient.Builder webClientBuilder,
            @Value("${pagamento.externo.url}") String baseUrl
    ) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }


    //    @Retry(name = "procpag", fallbackMethod = "fallback")
    @Override
    @CircuitBreaker(name = "procpag", fallbackMethod = "fallback")
    public void process(ExternalPaymentRequest request) {
        webClient.post()
                .uri("/requisicao")
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
        logger.info("Process payment calling fallback: {}", throwable.getMessage());
        throw new RuntimeException("External payment service unavailable", throwable);
    }
}