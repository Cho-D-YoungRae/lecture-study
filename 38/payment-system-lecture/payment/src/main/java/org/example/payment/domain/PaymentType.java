package org.example.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    NORMAL("일반 결제")
    ;

    private final String description;

    public static PaymentType get(String description) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("결제 타입이 올바르지 않습니다: " + description));
    }
}
