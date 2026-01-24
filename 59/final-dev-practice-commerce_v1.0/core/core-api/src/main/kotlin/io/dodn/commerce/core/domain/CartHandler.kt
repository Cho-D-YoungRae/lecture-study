package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CartType
import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CartAccessEntity
import io.dodn.commerce.storage.db.core.CartAccessRepository
import io.dodn.commerce.storage.db.core.CartEntity
import io.dodn.commerce.storage.db.core.CartRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class CartHandler(
    private val cartRepository: CartRepository,
    private val cartAccessRepository: CartAccessRepository,
) {
    companion object {
        private const val EXPIRATION_DAYS = 7L
    }

    fun createSharedCart(userId: Long): CartAccess {
        val cart = cartRepository.save(
            CartEntity(
                type = CartType.SHARED,
                userId = userId,
            ),
        )
        val access = cartAccessRepository.save(
            CartAccessEntity(
                accessKey = UUID.randomUUID().toString(), // NOTE: 어떤 방식이든 중복이 불가능한 값으로 구성
                cartId = cart.id,
                type = cart.type,
                userId = cart.userId,
                accessUserId = cart.userId,
                expiredAt = LocalDateTime.now().plusDays(EXPIRATION_DAYS),
            ),
        )
        return CartAccess(
            accessKey = access.accessKey,
            cartId = access.cartId,
            type = access.type,
            userId = access.userId,
            expiredAt = access.expiredAt,
            createdAt = access.createdAt,
            updatedAt = access.updatedAt,
        )
    }

    fun remove(userId: Long, cartId: Long): Long {
        val cart = cartRepository.findByIdAndUserIdAndStatus(cartId, userId, EntityStatus.ACTIVE)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (cart.type == CartType.DEFAULT) throw CoreException(ErrorType.CART_OPERATION_NOT_ALLOWED)
        cart.delete()
        return cart.id
    }

    fun access(userId: Long, accessKey: String) {
        val access = cartAccessRepository.findByAccessKey(accessKey)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        if (access.type == CartType.DEFAULT) throw CoreException(ErrorType.CART_OPERATION_NOT_ALLOWED)
        if (access.isExpired()) throw CoreException(ErrorType.CART_SHARED_EXPIRED)

        cartAccessRepository.save(
            CartAccessEntity(
                accessKey = "${access.cartId}-${access.userId}-$userId", // NOTE: 접근 완료에 대한 중복 없는 고유 값
                type = access.type,
                cartId = access.cartId,
                userId = access.userId, // NOTE: 공유 장바구니 주인
                accessUserId = userId, // NOTE: 공유 장바구니 접근 허용자 (로그인 유저)
                expiredAt = access.expiredAt,
            ),
        )
    }
}
