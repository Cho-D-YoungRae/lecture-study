package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service

@Service
class BrandService(
    private val brandFinder: BrandFinder,
) {
    fun findByIds(ids: Collection<Long>): List<Brand> = brandFinder.findByIds(ids)
}
