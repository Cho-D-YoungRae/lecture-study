package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.math.BigDecimal

/**
 * 재화이다보니 충돌을 방지하기 위해 낙관적 락을 위해 version 칼럼 추가
 */
@Entity
@Table(name = "point_balance")
class PointBalanceEntity(
    val userId: Long,
    balance: BigDecimal,
    @Version
    private var version: Long = 0,
) : BaseEntity() {
    var balance: BigDecimal = balance
        protected set

    fun apply(amount: BigDecimal) {
        balance += amount
    }
}
