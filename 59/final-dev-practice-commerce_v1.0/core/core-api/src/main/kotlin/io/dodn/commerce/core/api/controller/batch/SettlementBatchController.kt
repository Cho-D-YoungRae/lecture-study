package io.dodn.commerce.core.api.controller.batch

import io.dodn.commerce.core.domain.SettlementService
import io.dodn.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

/**
 * 정산 입금 배치만 변경되면 된다.
 * - 데이터 적재와 계산은 변경되지 않아도 된다.
 * - 매일매일 데이터를 모아놓으면 이 데이터들을 합쳐서 n일치 데이터를 만들 수 있다.
 * - 데이터는 매일매일 쌓아 놓는 것이 이후 다양한 요구사항에 더 유연하게 대처할 수 있다.
 */
@RestController
class SettlementBatchController(
    private val settlementService: SettlementService,
) {
    /**
     * NOTE: 정산 대상 적재 배치
     * - 오전 1시 실행
     * - 어제 00:00:00 ~ 23:59:59 데이터 기준으로 정산 데이터 적재
     */
    @PostMapping("/internal-batch/load-targets")
    fun loadTargets(@RequestParam targetDate: LocalDate = LocalDate.now()): ApiResponse<Any> {
        settlementService.loadTargets(
            targetDate = targetDate,
            from = targetDate.minusDays(1).atStartOfDay(),
            to = targetDate.atStartOfDay().minusNanos(1),
        )
        return ApiResponse.success()
    }

    /**
     * NOTE: 정산 계산 배치
     * - 오전 4시 실행
     */
    @PostMapping("/internal-batch/generate")
    fun calculate(@RequestParam targetDate: LocalDate = LocalDate.now()): ApiResponse<Any> {
        settlementService.generate(targetDate)
        return ApiResponse.success()
    }

    /**
     * NOTE: 정산 입금 배치
     * - 오전 9시 실행
     */
    @PostMapping("/internal-batch/transfer")
    fun transfer(@RequestParam targetDate: LocalDate = LocalDate.now()): ApiResponse<Any> {
        settlementService.transfer(targetDate)
        return ApiResponse.success()
    }
}
