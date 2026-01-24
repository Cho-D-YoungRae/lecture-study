package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CartType
import io.dodn.commerce.storage.db.core.CartEntity
import io.dodn.commerce.storage.db.core.CartRepository
import io.dodn.commerce.storage.db.core.CartShareEntity
import io.dodn.commerce.storage.db.core.CartShareRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.Base64

@Component
class CartShareManager(
    private val cartRepository: CartRepository,
    private val cartShareRepository: CartShareRepository,
) {
    private val random = SecureRandom()

    @Transactional
    fun createSharedCart(ownerUserId: Long, expireAfterHours: Long = 24): CartShareEntity {
        val cart = cartRepository.save(
            CartEntity(
                ownerUserId = ownerUserId,
                type = CartType.SHARED,
            ),
        )
        val token = generateToken()
        val share = CartShareEntity(
            cartId = cart.id,
            token = token,
            expiresAt = LocalDateTime.now().plusHours(expireAfterHours),
        )
        return cartShareRepository.save(share)
    }

    private fun generateToken(size: Int = 24): String {
        val bytes = ByteArray(size)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
}
