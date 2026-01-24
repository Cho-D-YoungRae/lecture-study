package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service

@Service
class CancelService(
    private val cancelValidator: CancelValidator,
    private val cancelProcessor: CancelProcessor,
    private val cancelCalculator: CancelCalculator,
) {
    fun cancel(user: User, action: CancelAction): Long {
        cancelValidator.validate(user, action)

        /**
         * NOTE: PG 취소 API 호출 => 성공 시 다음 로직으로 진행 | 실패 시 예외 발생
         */

        return cancelProcessor.cancel(action)
    }

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
