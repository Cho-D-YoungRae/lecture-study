package io.dodn.commerce.core.api.controller.batch

import io.dodn.commerce.core.domain.SettlementService
import io.dodn.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

/**
 * 스프링 배치 등을 사용하지는 않음 -> 데이터가 많지도 않음
 * 크론잡을 통해서 API 호출
 *
 * 큰 작업들을 작은 여러 작업으로 나누어 처리
 *
 * 시간베이스로 작업을 나눔
 * > 작업이 커져서 다음 시간 배치가 시작되기 전에 전 배치가 완료되지 않으면 다음 배치가 정상적으로 동작하지 않을 수 있음
 * > 데이터가 적을 떄는 충분히 쓸만한 전략
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
            settleDate = targetDate,
            from = targetDate.minusDays(1).atStartOfDay(),
            to = targetDate.atStartOfDay().minusNanos(1),
        )
        return ApiResponse.success()
    }

    /**
     * NOTE: 정산 계산 배치
     * - 오전 4시 실행
     */
    @PostMapping("/internal-batch/calculate")
    fun calculate(@RequestParam targetDate: LocalDate = LocalDate.now()): ApiResponse<Any> {
        settlementService.calculate(targetDate)
        return ApiResponse.success()
    }

    /**
     * NOTE: 정산 입금 배치
     * - 오전 9시 실행
     */
    @PostMapping("/internal-batch/transfer")
    fun transfer(): ApiResponse<Any> {
        settlementService.transfer()
        return ApiResponse.success()
    }
}
