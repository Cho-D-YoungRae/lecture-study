package org.example.payment.application.port.in;

public record PaymentConfirmCommand(
        String paymentKey,
        String orderId,
        long amount
) {
}
