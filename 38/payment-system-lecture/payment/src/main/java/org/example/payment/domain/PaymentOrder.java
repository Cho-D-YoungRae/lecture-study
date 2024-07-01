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
            @Nullable final Long id,
            @Nullable final Long paymentEventId,
            final long sellerId,
            final long productId,
            final String orderId,
            final long amount,
            @Nullable final PaymentStatus paymentStatus,
            @Nullable final Boolean ledgerUpdated,
            @Nullable final Boolean walletUpdated
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
            final long sellerId,
            final long productId,
            final String orderId,
            final long amount
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
