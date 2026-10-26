package org.example.payment.domain;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum PaymentMethod {

    EAZY_PAY("간편 결제"),
    ;

    private final String method;

    public static PaymentMethod get(String method) {
        return Arrays.stream(values())
                .filter(it -> it.method.equals(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("결제 수단이 올바르지 않습니다: " + method));
    }
}
