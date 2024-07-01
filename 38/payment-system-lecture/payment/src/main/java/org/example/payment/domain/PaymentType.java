package org.example.payment.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentType {
    NORMAL("일반 결제")
    ;

    private final String description;
}
