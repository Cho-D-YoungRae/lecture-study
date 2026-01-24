package io.dodn.commerce.core.domain

import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartReader: CartReader,
    private val cartVerifier: CartVerifier,
    private val cartHandler: CartHandler,
    private val cartItemManager: CartItemManager,
) {
    fun getCart(user: User): Cart {
        return cartReader.getCart(user.id)
    }

    fun getAccessibleCarts(user: User): List<CartAccess> {
        return cartReader.getCartAccessList(user.id)
    }

    fun getSharedCart(user: User, cartId: Long): Cart {
        return cartReader.getSharedCart(user.id, cartId)
    }

    fun createSharedCarts(user: User): CartAccess {
        return cartHandler.createSharedCart(user.id)
    }

    fun deleteCart(user: User, cartId: Long) {
        cartHandler.remove(user.id, cartId)
    }

    fun access(user: User, accessKey: String) {
        cartHandler.access(user.id, accessKey)
    }

    fun addCartItem(user: User, item: AddCartItem): Long {
        val owner = cartVerifier.verifyAccess(user.id, item.cartId)
        return cartItemManager.addItem(owner, item)
    }

    fun modifyCartItem(user: User, item: ModifyCartItem): Long {
        return cartItemManager.modifyItem(user.id, item)
    }

    fun deleteCartItem(user: User, cartItemId: Long) {
        cartItemManager.deleteItem(user.id, cartItemId)
    }
}
