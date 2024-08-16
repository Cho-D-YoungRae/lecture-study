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
            @Nullable Long id,
            long buyerId,
            String orderName,
            String orderId,
            @Nullable String paymentKey,
            @Nullable PaymentType paymentType,
            @Nullable PaymentMethod paymentMethod,
            @Nullable LocalDateTime approvedAt,
            @Nullable List<PaymentOrder> paymentOrders,
            @Nullable Boolean paymentDone
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
