package org.example.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.example.common.UseCase;
import org.example.payment.application.port.in.PaymentConfirmCommand;
import org.example.payment.application.port.in.PaymentConfirmUseCase;
import org.example.payment.application.port.out.PaymentExecutorPort;
import org.example.payment.application.port.out.PaymentStatusUpdateCommand;
import org.example.payment.application.port.out.PaymentStatusUpdatePort;
import org.example.payment.application.port.out.PaymentValidationPort;
import org.example.payment.domain.PaymentConfirmationResult;
import org.example.payment.domain.PaymentExecutionResult;

@UseCase
@RequiredArgsConstructor
public class PaymentConfirmService implements PaymentConfirmUseCase {

    private final PaymentStatusUpdatePort paymentStatusUpdatePort;
    private final PaymentValidationPort paymentValidationPort;
    private final PaymentExecutorPort paymentExecutorPort;

    @Override
    public PaymentConfirmationResult confirm(PaymentConfirmCommand command) {
        paymentStatusUpdatePort.updatePaymentStatusToExecuting(command.orderId(), command.paymentKey());
        paymentValidationPort.validate(command.orderId(), command.amount());

        PaymentExecutionResult paymentExecutionResult = paymentExecutorPort.execute(command);
        paymentStatusUpdatePort.updatePaymentStatus(new PaymentStatusUpdateCommand(
                paymentExecutionResult.paymentKey(),
                paymentExecutionResult.orderId(),
                paymentExecutionResult.paymentStatus(),
                paymentExecutionResult.extraDetails(),
                paymentExecutionResult.failure()
        ));
        return new PaymentConfirmationResult(
                paymentExecutionResult.paymentStatus(), paymentExecutionResult.failure());
    }
}
