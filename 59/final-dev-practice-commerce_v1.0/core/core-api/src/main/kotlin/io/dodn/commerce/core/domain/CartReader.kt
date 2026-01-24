package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CartType
import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CartAccessRepository
import io.dodn.commerce.storage.db.core.CartItemRepository
import io.dodn.commerce.storage.db.core.CartRepository
import org.springframework.stereotype.Component

@Component
class CartReader(
    private val cartItemRepository: CartItemRepository,
    private val productFinder: ProductFinder,
    private val productOptionFinder: ProductOptionFinder,
    private val cartRepository: CartRepository,
    private val cartAccessRepository: CartAccessRepository,
) {
    fun getCart(userId: Long): Cart {
        val carts = cartRepository.findByUserIdAndTypeAndStatus(userId, CartType.DEFAULT, EntityStatus.ACTIVE)
        if (carts.isEmpty()) throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (carts.size > 1) throw CoreException(ErrorType.DEFAULT_ERROR)

        return read(userId, carts[0].id)
    }

    fun getSharedCart(userId: Long, cartId: Long): Cart {
        val cart = cartRepository.findByIdAndTypeAndStatus(cartId, CartType.SHARED, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        val access = cartAccessRepository.findByCartIdAndAccessUserIdAndStatus(cart.id, userId, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.CART_SHARED_NOT_FOUND)
        if (access.isExpired()) throw CoreException(ErrorType.CART_SHARED_EXPIRED)

        return read(userId, cart.id)
    }

    fun getCartAccessList(userId: Long): List<CartAccess> {
        return cartAccessRepository.findByUserIdAndStatus(userId, EntityStatus.ACTIVE)
            .filter { it.isNotExpired() }
            .map {
                CartAccess(
                    accessKey = it.accessKey,
                    cartId = it.cartId,
                    type = it.type,
                    userId = it.userId,
                    expiredAt = it.expiredAt,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                )
            }
    }

    private fun read(userId: Long, cartId: Long): Cart {
        val items = cartItemRepository.findByUserIdAndCartIdAndStatus(userId, cartId, EntityStatus.ACTIVE)
        if (items.isEmpty()) {
            return Cart(userId = userId, items = emptyList())
        }

        val productIds = items.map { it.productId }
        val productOptionIds = items.map { it.productOptionId }
        val products = productFinder.find(productIds)
        val productOptions = productOptionFinder.find(productOptionIds, EntityStatus.ACTIVE)
        val productMap = products.associateBy { it.id }
        val productOptionMap = productOptions.associateBy { it.id }

        val mappedItems = items
            .filter { productMap.containsKey(it.productId) && productOptionMap.containsKey(it.productOptionId) }
            .map {
                CartItem(
                    id = it.id,
                    product = productMap[it.productId]!!,
                    productOption = productOptionMap[it.productOptionId]!!,
                    quantity = it.quantity,
                )
            }

        return Cart(
            userId = userId,
            items = mappedItems,
        )
    }
}
