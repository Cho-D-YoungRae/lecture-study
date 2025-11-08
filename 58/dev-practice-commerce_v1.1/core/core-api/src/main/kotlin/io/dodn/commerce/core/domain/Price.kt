package io.dodn.commerce.core.domain

import java.math.BigDecimal

/**
 * 지금은 없지만 실무에서는 금액 계산 같은 로직이 들어갈 수 있어서 Price 는 묶는 것이 좋음.
 */
data class Price(
    val costPrice: BigDecimal,
    val salesPrice: BigDecimal,
    val discountedPrice: BigDecimal,
)
