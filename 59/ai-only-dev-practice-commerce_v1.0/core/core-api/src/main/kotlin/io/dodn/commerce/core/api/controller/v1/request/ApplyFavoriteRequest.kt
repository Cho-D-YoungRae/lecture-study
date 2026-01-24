package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.enums.FavoriteTargetType

data class ApplyFavoriteRequest(
    val productId: Long? = null,
    val targetType: FavoriteTargetType? = null,
    val targetId: Long? = null,
    val type: ApplyFavoriteRequestType,
)

enum class ApplyFavoriteRequestType {
    FAVORITE,
    UNFAVORITE,
}
