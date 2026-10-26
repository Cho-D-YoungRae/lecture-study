package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.math.BigDecimal

/**
 * price 들이 Product 개념 클래스에는 따로 묶여 있지만, 여기서는 임베디드 타입으로 묶지 않음.
 * * 여기서는 엔티티와 개념 클래스를 분리하는 것을 선호해서 이렇게 설계함. 엔티티에서는 플랫하게 사용.
 * * 엔티티를 개념 클래스를 사용할 수도 있음.
 */
@Entity
@Table(name = "product")
class ProductEntity(
    val name: String,
    val thumbnailUrl: String,
    val description: String,
    val shortDescription: String,
    val costPrice: BigDecimal,
    val salesPrice: BigDecimal,
    val discountedPrice: BigDecimal,
) : BaseEntity()
