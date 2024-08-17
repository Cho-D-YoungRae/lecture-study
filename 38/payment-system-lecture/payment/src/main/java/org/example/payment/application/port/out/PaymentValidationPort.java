package org.example.payment.application.port.out;

public interface PaymentValidationPort {

    void validate(String orderId, long amount);
}
