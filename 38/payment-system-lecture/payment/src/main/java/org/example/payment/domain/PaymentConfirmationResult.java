package org.example.payment.domain;

import jakarta.annotation.Nullable;

public record PaymentConfirmationResult(
        PaymentStatus status,
        @Nullable PaymentExecutionResult.PaymentExecutionFailure failure
) {

    public PaymentConfirmationResult {
        if (PaymentStatus.SUCCESS == status && failure != null) {
            throw new IllegalArgumentException("결제 성공 상태는 실패 정보가 없어야 합니다.");
        }
        if (PaymentStatus.FAILURE == status && failure == null) {
            throw new IllegalArgumentException("결제 실패 상태는 실패 정보가 필요합니다.");
        }
    }

    public PaymentConfirmationResult(PaymentStatus status) {
        this(status, null);
    }

    public String message() {
        if (PaymentStatus.SUCCESS == status) {
            return "결제 처리에 성공하였습니다.";
        } else if(PaymentStatus.FAILURE == status) {
            return "결제 처리에 실패하였습니다.";
        } else if (PaymentStatus.UNKNOWN == status) {
            return "결제 처리 중에 알 수 없는 에러가 발생하였습니다.";
        } else {
            throw new IllegalStateException("현재 결제 상태(status:" + status + ")는 올바르지 않은 상태입니다.");
        }

    }
}
