package org.example.payment.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentMethod {

    EAZY_PAY("간편 결제"),
    ;

    private final String description;
}
