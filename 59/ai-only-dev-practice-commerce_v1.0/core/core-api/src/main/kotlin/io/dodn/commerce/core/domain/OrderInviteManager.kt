package io.dodn.commerce.core.domain

import io.dodn.commerce.storage.db.core.OrderInviteEntity
import io.dodn.commerce.storage.db.core.OrderInviteRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderInviteManager(
    private val orderInviteRepository: OrderInviteRepository,
    private val orderKeyGenerator: OrderKeyGenerator,
) {
    @Transactional
    fun create(orderId: Long): String {
        val inviteKey = orderKeyGenerator.generate()
        val entity = OrderInviteEntity(
            orderId = orderId,
            inviteKey = inviteKey,
        )
        orderInviteRepository.save(entity)
        return inviteKey
    }
}
