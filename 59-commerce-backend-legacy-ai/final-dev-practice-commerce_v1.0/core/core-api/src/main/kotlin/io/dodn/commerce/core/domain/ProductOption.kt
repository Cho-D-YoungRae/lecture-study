package io.dodn.commerce.core.domain

/**
 * 옵션이 생기면 이제 옵션의 가격이 중요해짐
 * > 상품의 가격은 노출용으로 쓰이는 등
 * 옵션이 추가되면 Product 로 구매가 되는 것이 아니라 옵션으로 구매되는 것
 */
data class ProductOption(
    val id: Long,
    val productId: Long,
    val name: String,
    val description: String,
    val price: Price,
)
