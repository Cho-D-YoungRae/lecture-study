package io.dodn.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface CancelBalanceRepository : JpaRepository<CancelBalanceEntity, Long> {
    fun findByOrderId(orderId: Long): CancelBalanceEntity?
}
