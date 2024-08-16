package org.example.payment.adapter.out.web.toss.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class TossPaymentExecutor {

    private static final String URI = "/v1/payments/confirm";

    private final RestClient tossPaymentRestClient;

    public String execute(TossPaymentExecution execution) {
        return tossPaymentRestClient.post()
                .uri(URI)
                .body(execution)
                .retrieve()
                .body(String.class);
    }
}
