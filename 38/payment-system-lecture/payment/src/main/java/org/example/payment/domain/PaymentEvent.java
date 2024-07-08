package org.example.payment.domain;

import jakarta.annotation.Nullable;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record PaymentEvent(
        @Nullable Long id,
        long buyerId,
        String orderName,
        String orderId,
        @Nullable String paymentKey,
        PaymentType paymentType,
        PaymentMethod paymentMethod,
        @Nullable LocalDateTime approvedAt,
        List<PaymentOrder> paymentOrders,
        boolean paymentDone
) {

    @Builder
    public PaymentEvent(
            @Nullable final Long id,
            final long buyerId,
            final String orderName,
            final String orderId,
            @Nullable final String paymentKey,
            @Nullable final PaymentType paymentType,
            @Nullable final PaymentMethod paymentMethod,
            @Nullable final LocalDateTime approvedAt,
            @Nullable final List<PaymentOrder> paymentOrders,
            @Nullable final Boolean paymentDone
    ) {
        this(
                id,
                buyerId,
                orderName,
                orderId,
                paymentKey,
                paymentType == null ? PaymentType.NORMAL : paymentType,
                paymentMethod == null ? PaymentMethod.EAZY_PAY : paymentMethod,
                approvedAt,
                paymentOrders != null ? paymentOrders : List.of(),
                paymentDone == null ? false : paymentDone
        );
    }

    public long totalAmount() {
        return paymentOrders.stream()
                .mapToLong(PaymentOrder::amount)
                .sum();
    }

}
