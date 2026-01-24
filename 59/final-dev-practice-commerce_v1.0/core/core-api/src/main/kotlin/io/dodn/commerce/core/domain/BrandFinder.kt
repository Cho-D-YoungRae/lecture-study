package io.dodn.commerce.core.domain

import io.dodn.commerce.storage.db.core.BrandRepository
import org.springframework.stereotype.Component

@Component
class BrandFinder(
    private val brandRepository: BrandRepository,
) {
    fun findByIds(ids: Collection<Long>): List<Brand> {
        if (ids.isEmpty()) return emptyList()
        return brandRepository.findAllById(ids).map { Brand(id = it.id, name = it.name) }
    }
}
