package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service

@Service
class MerchantService(
    private val merchantFinder: MerchantFinder,
) {
    fun findByIds(ids: Collection<Long>): List<Merchant> = merchantFinder.findByIds(ids)
}
