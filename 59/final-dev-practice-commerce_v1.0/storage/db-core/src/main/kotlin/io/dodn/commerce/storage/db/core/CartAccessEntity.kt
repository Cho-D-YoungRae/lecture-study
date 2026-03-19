package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.CartType
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime


/**
 * 공유 장바구니는 언젠가 지워질 수도 있음을 생각한다. -> CartAccessEntity가 액세스 키와 만료 관련 정보도 갖는다.
 * > 카트가 만료되는 것은 공유 장바구니로 인해 생긴 기능.
 */
@Entity
@Table(
    name = "cart_access",
    indexes = [
        Index(name = "udx_cart_access_key", columnList = "accessKey", unique = true),
        Index(name = "udx_cart_access_user", columnList = "cartId, accessUserId", unique = true),
    ],
)
class CartAccessEntity(
    val accessKey: String,
    val cartId: Long,
    val type: CartType,
    val userId: Long,
    val accessUserId: Long,
    val expiredAt: LocalDateTime,
) : BaseEntity() {
    fun isExpired(): Boolean {
        return expiredAt.isBefore(LocalDateTime.now())
    }

    fun isNotExpired(): Boolean {
        return !isExpired()
    }
}
