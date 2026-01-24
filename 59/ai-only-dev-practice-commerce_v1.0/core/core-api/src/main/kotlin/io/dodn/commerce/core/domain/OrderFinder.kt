package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.storage.db.core.OrderItemRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class OrderFinder(
    private val orderItemRepository: OrderItemRepository,
) {
    fun countOrdersByProductIds(productIds: List<Long>, fromDate: LocalDateTime): Map<Long, Long> {
        if (productIds.isEmpty()) {
            return emptyMap()
        }

        val results = orderItemRepository.countOrdersByProductIdsAndStateAndCreatedAtAfter(
            productIds = productIds,
            state = OrderState.PAID,
            fromDate = fromDate,
            status = EntityStatus.ACTIVE,
        )

        return results.associate { it.getTargetId() to it.getCount() }
    }
}
