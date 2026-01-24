package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.domain.ReviewContent
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.core.support.file.ImageHandle
import java.math.BigDecimal

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
