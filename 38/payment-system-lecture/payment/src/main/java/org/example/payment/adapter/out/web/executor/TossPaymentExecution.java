package org.example.payment.adapter.out.web.executor;

public record TossPaymentExecution(
        String paymentKey,
        String orderId,
        long amount
) {
}
