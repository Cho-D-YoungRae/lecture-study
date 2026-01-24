package io.dodn.commerce.core.domain

import io.dodn.commerce.storage.db.core.SettlementRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SettlementTransferHandler(
    private val settlementRepository: SettlementRepository,
) {
    @Transactional
    fun success(settlements: List<Settlement>) {
        val entities = settlementRepository.findAllById(settlements.map { it.id })
        entities.forEach { it.sent() }
        settlementRepository.saveAll(entities)
    }
}
