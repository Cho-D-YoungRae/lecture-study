package org.example.payment.adapter.in.web.api;

public record TossPaymentConfirmRequest(
        String paymentKey,
        String orderId,
        long amount
) {
}
