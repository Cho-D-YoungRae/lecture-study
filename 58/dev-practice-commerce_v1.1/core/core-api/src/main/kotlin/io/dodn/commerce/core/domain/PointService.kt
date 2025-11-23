package io.dodn.commerce.core.domain

import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.PointBalanceRepository
import io.dodn.commerce.storage.db.core.PointHistoryRepository
import org.springframework.stereotype.Component

@Component
class PointService(
    private val pointBalanceRepository: PointBalanceRepository,
    private val pointHistoryRepository: PointHistoryRepository,
) {
    /**
     * 현재는 최종적인 포인트 잔액만 보여주는 요구사항(건별로 포인트를 관리하지 않음)
     * * 우리는 건별로 관리해야되는 것인가? 를 고민해보고 그런 요구사항이 왔을 때 간단하게 가자고 요구해볼 수도 있는 것.
     */
    fun balance(user: User): PointBalance {
        val found = pointBalanceRepository.findByUserId(user.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        return PointBalance(
            userId = found.userId,
            balance = found.balance,
        )
    }

    fun histories(user: User): List<PointHistory> {
        return pointHistoryRepository.findByUserId(user.id)
            .map {
                PointHistory(
                    id = it.id,
                    userId = it.userId,
                    type = it.type,
                    referenceId = it.referenceId,
                    amount = it.amount,
                    appliedAt = it.createdAt,
                )
            }
    }
}
