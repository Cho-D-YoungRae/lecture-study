package org.example.payment.domain;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public record PaymentExecutionResult(
        String paymentKey,
        String orderId,
        @Nullable PaymentExtraDetails extraDetails,
        @Nullable PaymentExecutionFailure failure,
        boolean isSuccess,
        boolean isFailure,
        boolean isUnknown,
        boolean isRetryable
) {

    public PaymentExecutionResult {
        if (Stream.of(isSuccess, isFailure, isUnknown)
                .filter(b -> b)
                .count() != 1) {
            throw new IllegalArgumentException("결제 (orderId: " + orderId + ") 결과는 성공, 실패, 불명확 중 하나여야 합니다.");
        }
    }

    public PaymentExecutionResult(
            String paymentKey,
            String orderId,
            boolean isSuccess,
            boolean isFailure,
            boolean isUnknown,
            boolean isRetryable
    ) {
        this(paymentKey, orderId, null, null, isSuccess, isFailure, isUnknown, isRetryable);
    }

    public PaymentStatus paymentStatus() {
        if (isSuccess) {
            return PaymentStatus.SUCCESS;
        } else if (isFailure) {
            return PaymentStatus.FAILURE;
        } else if (isUnknown) {
            return PaymentStatus.UNKNOWN;
        } else {
            throw new IllegalStateException("결제 (orderId: " + orderId + ") 는 올바르지 않은 결제 상태 입니다.");
        }
    }

    public record PaymentExtraDetails(
            PaymentType type,
            PaymentMethod method,
            LocalDateTime approvedAt,
            String orderName,
            PSPConfirmationStatus pspConfirmationStatus,
            long totalAmount,
            String pspRawData
    ) {
    }

    public record PaymentExecutionFailure(
            String errorCode,
            String message
    ) {
    }
}
