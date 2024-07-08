package org.example.payment.application.port.out;

import org.example.payment.domain.PaymentEvent;

public interface SavePaymentPort {

    long save(PaymentEvent paymentEvent);
}
