package org.example.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PSPConfirmationStatus {
    DONE("완료"),
    CANCELED("승인된 결제가 취소된 상태"),
    EXPIRED("결제 유효 시간이 지나서 만료된 상태"),
    PARTIAL_CANCELED("승인된 결제가 부분 취소된 상태"),
    ABORTED("결제 승인이 실패된 상태"),
    WAITING_FOR_DEPOSIT("가상계좌 결제 흐름에만 있는 상태로, 결제 고객이 발급된 가상 계좌에 입금하는 것을 기다리고 있는 상태"),
    IN_PROGRESS("결제수단 정보와 해당 결제 수단의 소유자가 맞는지 인증을 마친 상태"),
    READY("결제를 생성하면 가지게 되는 초기 상태")
    ;

    private final String description;

    public static PSPConfirmationStatus get(String status) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("PSP 승인 상태가 올바르지 않습니다: " + status));
    }
}
