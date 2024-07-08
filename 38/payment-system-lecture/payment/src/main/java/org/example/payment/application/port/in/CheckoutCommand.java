package org.example.payment.application.port.in;

import lombok.Builder;

import java.util.List;

@Builder
public record CheckoutCommand(
        long cartId,
        long buyerId,
        List<Long> productIds,
        // CheckoutRequest 와 비슷하지만 멱등성을 보장하기 위한 키를 갖음
        String idempotencyKey
) {
}
