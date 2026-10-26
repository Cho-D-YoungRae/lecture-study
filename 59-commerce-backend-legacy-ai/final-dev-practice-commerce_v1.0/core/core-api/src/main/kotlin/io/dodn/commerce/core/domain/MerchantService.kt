package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service

@Service
class MerchantService(
    private val merchantFinder: MerchantFinder,
) {
    fun find(ids: Collection<Long>): List<Merchant> {
        return merchantFinder.find(ids)
    }
}
