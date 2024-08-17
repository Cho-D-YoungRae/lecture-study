package org.example.payment.application.port.out;

import org.example.payment.application.port.in.PaymentConfirmCommand;
import org.example.payment.domain.PaymentExecutionResult;

public interface PaymentExecutorPort {

    PaymentExecutionResult execute(PaymentConfirmCommand command);
}
