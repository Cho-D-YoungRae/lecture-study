package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.domain.NewOrder
import io.dodn.commerce.core.domain.NewOrderItem
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType

/**
 * 강사는 클래스 안에 클래스 만드는 것을 선호하지 않지만 프레젠테이션 레이어에서는 스펙을 보여줘야 되기도 하다보니 어느 정도 허용
 */
data class CreateOrderRequest(
    val targets: List<CreateOrderTarget>,
) {
    data class CreateOrderTarget(
        val productId: Long,
        val productOptionId: Long,
        val quantity: Long,
    )

    fun toNewOrder(user: User): NewOrder {
        if (targets.isEmpty()) throw CoreException(ErrorType.INVALID_REQUEST)
        if (targets.any { it.quantity <= 0 }) throw CoreException(ErrorType.INVALID_REQUEST)

        return NewOrder(
            userId = user.id,
            items = targets.map {
                NewOrderItem(
                    productId = it.productId,
                    productOptionId = it.productOptionId,
                    quantity = it.quantity,
                )
            },
        )
    }
}
