package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CartType
import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CartAccessRepository
import io.dodn.commerce.storage.db.core.CartRepository
import org.springframework.stereotype.Component

@Component
class CartVerifier(
    private val cartRepository: CartRepository,
    private val cartAccessRepository: CartAccessRepository,
) {
    fun verifyAccess(userId: Long, cartId: Long): CartOwner {
        val cart = cartRepository.findByIdAndStatus(cartId, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)

        when (cart.type) {
            CartType.DEFAULT -> {
                if (cart.userId != userId) throw CoreException(ErrorType.INVALID_REQUEST)
            }

            CartType.SHARED -> {
                val access = cartAccessRepository.findByCartIdAndAccessUserIdAndStatus(cart.id, userId, EntityStatus.ACTIVE)
                    ?: throw CoreException(ErrorType.CART_SHARED_NOT_FOUND)
                if (access.isExpired()) throw CoreException(ErrorType.CART_SHARED_EXPIRED)
            }
        }
        return CartOwner(cart.id, cart.userId)
    }
}
