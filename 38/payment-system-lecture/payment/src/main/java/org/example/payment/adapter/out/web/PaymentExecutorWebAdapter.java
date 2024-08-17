package org.example.payment.adapter.out.web;

import lombok.RequiredArgsConstructor;
import org.example.common.WebAdapter;
import org.example.payment.adapter.out.web.toss.executor.PaymentExecutor;
import org.example.payment.application.port.in.PaymentConfirmCommand;
import org.example.payment.application.port.out.PaymentExecutorPort;
import org.example.payment.domain.PaymentExecutionResult;

@WebAdapter
@RequiredArgsConstructor
public class PaymentExecutorWebAdapter implements PaymentExecutorPort {

    private final PaymentExecutor paymentExecutor;

    @Override
    public PaymentExecutionResult execute(PaymentConfirmCommand command) {
        return paymentExecutor.execute(command);
    }
}
