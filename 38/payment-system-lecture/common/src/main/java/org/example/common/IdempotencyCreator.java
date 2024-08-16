package org.example.common;

import java.util.UUID;

public final class IdempotencyCreator {

    private IdempotencyCreator() {
        throw new UnsupportedOperationException();
    }

    /**
     * 구매하기 버튼이 연속으로 눌려서 Checkout API 가 여러번 호출되었을 때
     * 호출된 API 수 만큼 Payment Event 와 Payment Order 가 만들어지지 않도록 하기 위해
     * 사용되는 요청 본문 데이터를 바탕으로 고유한 orderId 를 생성
     */
    public static String create(Object data) {
        return UUID.nameUUIDFromBytes(data.toString().getBytes()).toString();
    }
}
