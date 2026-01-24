package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CartItemEntity
import io.dodn.commerce.storage.db.core.CartItemRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CartItemManager(
    private val cartItemRepository: CartItemRepository,
) {
    @Transactional
    fun addItem(owner: CartOwner, item: AddCartItem): Long {
        val existing = cartItemRepository.findByCartIdAndProductIdAndProductOptionId(owner.cartId, item.productId, item.productOptionId)
        if (existing != null) {
            if (existing.isDeleted()) existing.active()
            existing.applyQuantity(item.quantity)
            return existing.id
        }
        return cartItemRepository.save(
            CartItemEntity(
                userId = owner.cartOwnerId, // NOTE: 장바구니 주인의 userId로 기입
                cartId = owner.cartId,
                productId = item.productId,
                productOptionId = item.productOptionId,
                quantity = item.quantity,
            ),
        ).id
    }

    @Transactional
    fun modifyItem(userId: Long, item: ModifyCartItem): Long {
        val found = cartItemRepository.findByUserIdAndIdAndStatus(userId, item.cartItemId, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.applyQuantity(item.quantity)
        return found.id
    }

    @Transactional
    fun deleteItem(userId: Long, cartItemId: Long) {
        val entity = cartItemRepository.findByUserIdAndIdAndStatus(userId, cartItemId, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        entity.delete()
    }

    @Transactional
    fun deleteItemsByProductOptions(userId: Long, productOptionIds: List<Long>) {
        cartItemRepository.findByUserIdAndProductOptionIdInAndStatus(userId, productOptionIds, EntityStatus.ACTIVE)
            .forEach { it.delete() }
    }
}
