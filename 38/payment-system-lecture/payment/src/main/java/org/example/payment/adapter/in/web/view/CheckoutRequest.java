package org.example.payment.adapter.in.web.view;

import java.time.LocalDate;
import java.util.List;

public record CheckoutRequest(
        Long cartId,
        List<Long> productIds,
        Long buyerId,
        // 요청을 구별해줄 seed 값
        // 이 값이 없으면 구입하는 상품이 같은 경우 요청을 구별할 수 없음
        String seed
) {

    public CheckoutRequest {
        // checkout 요청을 사용자가 수동으로 하지 않고 자동으로 호출되도록 할것 (강의용)
        // -> 기본 값 설정
        if (cartId == null) {
            cartId = 1L;
        }
        if (productIds == null) {
            productIds = List.of(1L, 2L, 3L);
        }
        if (buyerId == null) {
            buyerId = 1L;
        }
        if (seed == null) {
            seed = LocalDate.now().toString();
        }
    }
}
