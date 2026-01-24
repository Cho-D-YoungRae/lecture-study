package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.domain.AddCartItem
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType

data class AddCartItemRequest(
    val productId: Long,
    val productOptionId: Long,
    val quantity: Long,
    val sharedCartId: Long? = null,
) {
    fun toAddCartItem(): AddCartItem {
        if (quantity <= 0) throw CoreException(ErrorType.INVALID_REQUEST)
        return AddCartItem(productId, quantity, productOptionId, sharedCartId)
    }
}
