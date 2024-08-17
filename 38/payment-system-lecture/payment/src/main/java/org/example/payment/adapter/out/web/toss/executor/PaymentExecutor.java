package org.example.payment.adapter.out.web.toss.executor;

import org.example.payment.application.port.in.PaymentConfirmCommand;
import org.example.payment.domain.PaymentExecutionResult;

public interface PaymentExecutor {

    PaymentExecutionResult execute(PaymentConfirmCommand command);
}
