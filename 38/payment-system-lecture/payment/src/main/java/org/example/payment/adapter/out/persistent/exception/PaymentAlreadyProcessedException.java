package org.example.payment.adapter.out.persistent.exception;

import lombok.Getter;
import org.example.payment.domain.PaymentStatus;

@Getter
public class PaymentAlreadyProcessedException extends RuntimeException {

    private final PaymentStatus status;

    public PaymentAlreadyProcessedException(PaymentStatus status, String message) {
        super(message + " status: " + status);
        this.status = status;
    }
}
