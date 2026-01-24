package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.SettlementState
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface SettlementRepository : JpaRepository<SettlementEntity, Long> {
    fun findByState(state: SettlementState): List<SettlementEntity>

    @Query(
        """
        SELECT s.merchantId as merchantId, SUM(s.originalAmount) as amount 
        FROM SettlementEntity s 
        WHERE s.merchantId IN :merchantIds 
          AND s.settlementDate >= :startDate 
          AND s.settlementDate <= :endDate
          AND s.state = :state
        GROUP BY s.merchantId
    """,
    )
    fun sumOriginalAmountByMerchantIdInAndSettlementDateBetweenAndState(
        merchantIds: Collection<Long>,
        startDate: LocalDate,
        endDate: LocalDate,
        state: SettlementState,
    ): List<MerchantAmountProjection>
}
