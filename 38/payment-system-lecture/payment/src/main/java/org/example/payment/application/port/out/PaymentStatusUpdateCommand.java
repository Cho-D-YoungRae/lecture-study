package org.example.payment.application.port.out;

import jakarta.annotation.Nullable;
import org.example.payment.domain.PaymentExecutionResult;
import org.example.payment.domain.PaymentStatus;

public record PaymentStatusUpdateCommand(
        String paymentKey,
        String orderId,
        PaymentStatus status,
        @Nullable PaymentExecutionResult.PaymentExtraDetails extraDetails,
        @Nullable PaymentExecutionResult.PaymentExecutionFailure failure
) {

    public PaymentStatusUpdateCommand {
        if (!(PaymentStatus.SUCCESS == status || PaymentStatus.FAILURE == status || PaymentStatus.UNKNOWN == status)) {
            throw new IllegalArgumentException("결제 상태(status:" + status + ")는 올바르지 않은 정보입니다.");
        }

        if (PaymentStatus.SUCCESS == status && extraDetails == null) {
            throw new IllegalArgumentException("결제 성공 상태는 추가 정보(extraDetails)가 필요합니다.");
        } else if (PaymentStatus.FAILURE == status && failure == null) {
            throw new IllegalArgumentException("결제 실패 상태는 실패 정보(failure)가 필요합니다.");
        }
    }

    public PaymentStatusUpdateCommand(
            String paymentKey, String orderId, PaymentStatus status) {
        this(paymentKey, orderId, status, null, null);
    }
}
