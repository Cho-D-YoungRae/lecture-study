package org.example.payment.adapter.out.web.toss.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TossPaymentExecutor {

    private static final String URI = "/v1/payments/confirm";

    private final WebClient tossPaymentWebClient;

    public Mono<String> execute(final TossPaymentExecution execution) {
        return tossPaymentWebClient.post()
                .uri(URI)
                .bodyValue(execution)
                .retrieve()
                .bodyToMono(String.class);
    }
}
