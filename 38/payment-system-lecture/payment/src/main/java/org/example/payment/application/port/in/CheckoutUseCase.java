package org.example.payment.application.port.in;

import org.example.payment.domain.CheckoutResult;

public interface CheckoutUseCase {

    CheckoutResult checkout(CheckoutCommand command);
}
