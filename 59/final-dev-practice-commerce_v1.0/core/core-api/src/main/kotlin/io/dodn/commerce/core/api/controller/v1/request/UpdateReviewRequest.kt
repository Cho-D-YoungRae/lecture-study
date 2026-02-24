package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.domain.ReviewContent
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.core.support.file.ImageHandle
import java.math.BigDecimal

/**
 * 리뷰 컨텐츠와 이미지는 성질이 다르고, 라이프사이클이 다르다고 판단
 * > 별도 클래스로 구성을 함
 */
data class UpdateReviewRequest(
    val rate: BigDecimal,
    val content: String,
    val images: List<Long>?,
    val deleteImageIds: List<Long>?,
) {
    fun toContent(): ReviewContent {
        if (rate <= BigDecimal.ZERO) throw CoreException(ErrorType.INVALID_REQUEST)
        if (rate > BigDecimal.valueOf(5.0)) throw CoreException(ErrorType.INVALID_REQUEST)
        if (content.isEmpty()) throw CoreException(ErrorType.INVALID_REQUEST)
        return ReviewContent(rate, content)
    }

    fun toImageHandle(): ImageHandle {
        val list = images ?: emptyList()
        if (list.size > 5) throw CoreException(ErrorType.INVALID_REQUEST)
        return ImageHandle(list, deleteImageIds ?: emptyList())
    }
}
