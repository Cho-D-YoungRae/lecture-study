package org.example.payment.domain;

import jakarta.annotation.Nullable;
import lombok.Builder;

public record PaymentOrder(
        @Nullable Long id,
        @Nullable Long paymentEventId,
        long sellerId,
        long productId,
        String orderId,
        long amount,
        PaymentStatus paymentStatus,
        boolean ledgerUpdated,
        boolean walletUpdated
) {

    @Builder
    public PaymentOrder(
            @Nullable Long id,
            @Nullable Long paymentEventId,
            long sellerId,
            long productId,
            String orderId,
            long amount,
            @Nullable PaymentStatus paymentStatus,
            @Nullable Boolean ledgerUpdated,
            @Nullable Boolean walletUpdated
    ) {
        this(
                id,
                paymentEventId,
                sellerId,
                productId,
                orderId,
                amount,
                paymentStatus == null ? PaymentStatus.NOT_STARTED : paymentStatus,
                ledgerUpdated == null ? false : ledgerUpdated,
                walletUpdated == null ? false : walletUpdated
        );
    }

    public PaymentOrder(
            long sellerId,
            long productId,
            String orderId,
            long amount
    ) {
        this(
                null,
                null,
                sellerId,
                productId,
                orderId,
                amount,
                PaymentStatus.NOT_STARTED,
                false,
                false
        );
    }
}
