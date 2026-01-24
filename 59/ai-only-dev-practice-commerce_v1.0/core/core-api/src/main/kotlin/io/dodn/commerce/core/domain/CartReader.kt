package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CartEntity
import io.dodn.commerce.storage.db.core.CartItemRepository
import io.dodn.commerce.storage.db.core.CartRepository
import io.dodn.commerce.storage.db.core.CartShareEntity
import io.dodn.commerce.storage.db.core.CartShareRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CartReader(
    private val cartItemRepository: CartItemRepository,
    private val productFinder: ProductFinder,
    private val productOptionFinder: ProductOptionFinder,
    private val cartRepository: CartRepository,
    private val cartShareRepository: CartShareRepository,
) {
    data class ShareView(val cart: CartEntity, val share: CartShareEntity)

    fun getCart(userId: Long): Cart {
        val items = cartItemRepository.findByUserIdAndStatus(userId, EntityStatus.ACTIVE)
        if (items.isEmpty()) {
            return Cart(userId = userId, items = emptyList())
        }

        val productIds = items.map { it.productId }
        val productOptionIds = items.map { it.productOptionId }
        val products = productFinder.find(productIds)
        val productOptions = productOptionFinder.findActive(productOptionIds)
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

    fun getActiveShareById(cartId: Long, now: LocalDateTime = LocalDateTime.now()): ShareView {
        val cart = cartRepository.findByIdAndStatus(cartId, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.SHARED_CART_NOT_FOUND_OR_DELETED)
        val share = cartShareRepository.findByCartIdAndStatus(cartId, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.SHARED_CART_NOT_FOUND_OR_DELETED)
        if (share.isExpired(now)) throw CoreException(ErrorType.SHARED_CART_EXPIRED)
        return ShareView(cart, share)
    }

    fun getActiveShareByToken(token: String, now: LocalDateTime = LocalDateTime.now()): ShareView {
        val share = cartShareRepository.findByTokenAndStatus(token, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.SHARED_CART_NOT_FOUND_OR_DELETED)
        if (share.isExpired(now)) throw CoreException(ErrorType.SHARED_CART_EXPIRED)
        val cart = cartRepository.findByIdAndStatus(share.cartId, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.SHARED_CART_NOT_FOUND_OR_DELETED)
        return ShareView(cart, share)
    }
}
