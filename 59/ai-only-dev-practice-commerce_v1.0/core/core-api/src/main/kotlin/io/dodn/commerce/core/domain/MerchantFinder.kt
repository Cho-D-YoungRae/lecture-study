package io.dodn.commerce.core.domain

import io.dodn.commerce.storage.db.core.MerchantRepository
import org.springframework.stereotype.Component

@Component
class MerchantFinder(
    private val merchantRepository: MerchantRepository,
) {
    fun findByIds(ids: Collection<Long>): List<Merchant> {
        if (ids.isEmpty()) return emptyList()
        return merchantRepository.findAllById(ids)
            .map { Merchant(id = it.id, name = it.name, settlementCycle = it.settlementCycle) }
    }

    fun findAll(): List<Merchant> {
        return merchantRepository.findAll().map {
            Merchant(
                id = it.id,
                name = it.name,
                settlementCycle = it.settlementCycle,
            )
        }
    }
}
