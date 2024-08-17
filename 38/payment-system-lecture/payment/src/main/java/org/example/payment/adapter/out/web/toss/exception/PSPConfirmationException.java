package org.example.payment.adapter.out.web.toss.exception;

import lombok.Getter;
import org.example.payment.domain.PaymentStatus;

@Getter
public class PSPConfirmationException extends RuntimeException{

    private final String errorCode;
    private final String errorMessage;
    private final boolean isSuccess;
    private final boolean isFailure;
    private final boolean isUnknown;
    private final boolean isRetryable;

    public PSPConfirmationException(
            String errorCode,
            String errorMessage,
            boolean isSuccess,
            boolean isFailure,
            boolean isUnknown,
            boolean isRetryable,
            Exception cause
    ) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.isSuccess = isSuccess;
        this.isFailure = isFailure;
        this.isUnknown = isUnknown;
        this.isRetryable = isRetryable;
    }

    public PSPConfirmationException(
            String errorCode,
            String errorMessage,
            boolean isSuccess,
            boolean isFailure,
            boolean isUnknown,
            boolean isRetryable
    ) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.isSuccess = isSuccess;
        this.isFailure = isFailure;
        this.isUnknown = isUnknown;
        this.isRetryable = isRetryable;
    }

    public PaymentStatus paymentStatus() {
        if (isSuccess) {
            return PaymentStatus.SUCCESS;
        } else if (isFailure) {
            return PaymentStatus.FAILURE;
        } else if (isUnknown) {
            return PaymentStatus.UNKNOWN;
        } else {
            throw new IllegalStateException();
        }
    }
}
