package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.CancelType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * 부분 취소일 때는 orderItemId 가 없을 것
 * > null 혹은 -1 등의 값 활용 (강사는 -1 사용)
 */
@Entity
@Table(name = "cancel")
class CancelEntity(
    val userId: Long,
    @Enumerated(EnumType.STRING)
    val type: CancelType,
    val orderId: Long,
    val orderItemId: Long,
    val paymentId: Long,
    val originAmount: BigDecimal,
    val ownedCouponId: Long,
    val couponDiscount: BigDecimal,
    val usedPoint: BigDecimal,
    val paidAmount: BigDecimal,
    val canceledQuantity: Long,
    val canceledPaidAmount: BigDecimal,
    val canceledPointAmount: BigDecimal,
    val canceledCouponAmount: BigDecimal,
    val externalCancelKey: String,
    val canceledAt: LocalDateTime,
) : BaseEntity()
