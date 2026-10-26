package io.dodn.commerce.storage.db.core

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface CancelRepository : JpaRepository<CancelEntity, Long> {
    fun countByOrderId(orderId: Long): Long
    fun findAllByCanceledAtBetween(from: LocalDateTime, to: LocalDateTime, pageable: Pageable): Slice<CancelEntity>
}
