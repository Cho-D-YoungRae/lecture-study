package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service

@Service
class BrandService(
    private val brandFinder: BrandFinder,
) {
    fun find(ids: Collection<Long>): List<Brand> {
        return brandFinder.findByIds(ids)
    }
}
