package org.example.payment.domain;

public record CheckoutResult(
        long amount,
        String orderId,
        String orderName
) {
}
