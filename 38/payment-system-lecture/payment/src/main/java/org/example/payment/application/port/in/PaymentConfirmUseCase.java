package org.example.payment.application.port.in;

import org.example.payment.domain.PaymentConfirmationResult;

public interface PaymentConfirmUseCase {

    PaymentConfirmationResult confirm(PaymentConfirmCommand command);
}
