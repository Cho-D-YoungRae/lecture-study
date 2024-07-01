package org.example.payment.adapter.out.web.toss.executor;

public record TossPaymentExecution(
        String paymentKey,
        String orderId,
        long amount
) {
}
