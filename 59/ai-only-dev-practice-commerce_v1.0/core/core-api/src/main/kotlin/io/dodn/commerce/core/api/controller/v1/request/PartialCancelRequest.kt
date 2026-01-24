package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.domain.PartialCancelAction

data class PartialCancelRequest(
    val orderKey: String,
    val orderItemId: Long,
    val quantity: Long,
) {
    fun toPartialCancelAction(): PartialCancelAction {
        return PartialCancelAction(
            orderKey = orderKey,
            orderItemId = orderItemId,
            quantity = quantity,
        )
    }
}
