package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service

@Service
class CancelService(
    private val cancelValidator: CancelValidator,
    private val cancelProcessor: CancelProcessor,
    private val cancelCalculator: CancelCalculator,
) {
    /**
     * PaymentService 는 비슷하게 구현되어 있음.
     * > post process 없음 -> 장바구니를 복원하는 등의 요구사항이 있다면 그 부분도 대칭이 될 수 있음
     */
    fun cancel(user: User, action: CancelAction): Long {
        cancelValidator.validate(user, action)

        /**
         * NOTE: PG 취소 API 호출 => 성공 시 다음 로직으로 진행 | 실패 시 예외 발생
         */

        return cancelProcessor.cancel(action)
    }

    /**
     * 전체 취소랑 다른 부분은 밸리데이션을 끝내고 취소할 금액을 모르면 PG 쪽에 취소 금액을 보낼 때 얼만 지 알 수 없다.
     */
    fun partialCancel(user: User, action: PartialCancelAction): Long {
        cancelValidator.validatePartial(user, action)

        val calculated = cancelCalculator.calculatePartial(action)

        /**
         * NOTE: PG 부분 취소 API 호출 => 성공 시 다음 로직으로 진행 | 실패 시 예외 발생
         * calculateResult.paidAmount 만큼 PG 취소 요청
         */

        return cancelProcessor.partialCancel(action, calculated)
    }
}
