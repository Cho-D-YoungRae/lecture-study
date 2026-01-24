package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartReader: CartReader,
    private val cartItemManager: CartItemManager,
) {
    fun getCart(user: User): Cart {
        return cartReader.getCart(user.id)
    }

    fun addCartItem(user: User, item: AddCartItem): Long {
        // sharedCartId가 없으면 기존 개인 장바구니 경로 유지
        if (item.sharedCartId == null) {
            return cartItemManager.addItem(user.id, item)
        }

        // 공유 카트 경로: CartReader를 통해 유효성/만료 검증 후 cartId 기반 매니저에 위임
        val shareView = cartReader.getActiveShareById(item.sharedCartId)
        return cartItemManager.addItemByCart(shareView.cart.id, item.productId, item.productOptionId, item.quantity)
    }

    fun modifyCartItem(user: User, item: ModifyCartItem): Long {
        return cartItemManager.modifyItem(user.id, item)
    }

    fun deleteCartItem(user: User, cartItemId: Long) {
        cartItemManager.deleteItem(user.id, cartItemId)
    }
}
