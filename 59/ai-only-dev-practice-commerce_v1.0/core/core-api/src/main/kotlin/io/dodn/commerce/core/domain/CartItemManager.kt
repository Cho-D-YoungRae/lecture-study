package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CartType
import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CartEntity
import io.dodn.commerce.storage.db.core.CartItemEntity
import io.dodn.commerce.storage.db.core.CartItemRepository
import io.dodn.commerce.storage.db.core.CartRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CartItemManager(
    private val cartItemRepository: CartItemRepository,
    private val cartRepository: CartRepository,
) {
    @Transactional
    fun addItem(userId: Long, item: AddCartItem): Long {
        // 개인 Cart 확보(없으면 생성)
        val personalCart = getOrCreatePersonalCart(userId)

        val existing = cartItemRepository.findByCartIdAndProductId(personalCart.id, item.productId)
        if (existing != null) {
            if (existing.isDeleted()) existing.active()
            existing.applyQuantity(item.quantity.toLong())
            return existing.id
        }

        return cartItemRepository.save(
            CartItemEntity(
                userId = userId,
                cartId = personalCart.id,
                productId = item.productId,
                productOptionId = item.productOptionId,
                quantity = item.quantity,
            ),
        ).id
    }

    // cartId 기반(공유 카트 등) 아이템 추가 경로
    @Transactional
    fun addItemByCart(cartId: Long, productId: Long, productOptionId: Long, quantity: Long): Long {
        val existing = cartItemRepository.findByCartIdAndProductId(cartId, productId)
        if (existing != null) {
            if (existing.isDeleted()) existing.active()
            existing.applyQuantity(quantity)
            return existing.id
        }

        // cartId 소유자 조회(카트가 반드시 존재해야 함)
        val cart: CartEntity = cartRepository.findById(cartId).orElseThrow {
            CoreException(ErrorType.NOT_FOUND_DATA)
        }

        return cartItemRepository.save(
            CartItemEntity(
                userId = cart.ownerUserId,
                cartId = cartId,
                productId = productId,
                productOptionId = productOptionId,
                quantity = quantity,
            ),
        ).id
    }

    @Transactional
    fun modifyItem(userId: Long, item: ModifyCartItem): Long {
        val personalCart = getOrCreatePersonalCart(userId)
        val found = cartItemRepository.findByCartIdAndIdAndStatus(personalCart.id, item.cartItemId, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.applyQuantity(item.quantity.toLong())
        return found.id
    }

    @Transactional
    fun deleteItem(userId: Long, cartItemId: Long) {
        val personalCart = getOrCreatePersonalCart(userId)
        val entity = cartItemRepository.findByCartIdAndIdAndStatus(personalCart.id, cartItemId, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        entity.delete()
    }

    private fun getOrCreatePersonalCart(userId: Long): CartEntity {
        val existing = cartRepository.findByOwnerUserIdAndTypeAndStatus(userId, CartType.PERSONAL, EntityStatus.ACTIVE)
        if (existing != null) return existing
        return cartRepository.save(CartEntity(ownerUserId = userId, type = CartType.PERSONAL))
    }

    @Transactional
    fun deleteItemsByProductOptions(userId: Long, productOptionIds: List<Long>) {
        val cartItems = cartItemRepository.findByUserIdAndStatus(userId, EntityStatus.ACTIVE)
        cartItems.filter { productOptionIds.contains(it.productOptionId) }
            .forEach { it.delete() }
    }
}
