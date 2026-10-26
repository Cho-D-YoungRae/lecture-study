package io.dodn.commerce.core.domain

import java.math.BigDecimal

data class ReviewProcessResult(
    val id: Long,
    val format: ReviewFormat,
) {
    fun pointAmount(): BigDecimal {
        return when (format) {
            ReviewFormat.TEXT -> PointAmount.TEXT_REVIEW
            ReviewFormat.IMAGE -> PointAmount.IMAGE_REVIEW
        }
    }
}
