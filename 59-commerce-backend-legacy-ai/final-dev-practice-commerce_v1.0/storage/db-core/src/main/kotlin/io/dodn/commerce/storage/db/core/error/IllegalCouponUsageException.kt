package io.dodn.commerce.storage.db.core.error

/**
 * 현재 시스템에서는 core 쪽의 예외를 사용할 수는 없음
 *
 * > 저변인 db 쪽에 이 예외를 정의하고 core 에서 예외를 받아서 처리하도록 함
 */
class IllegalCouponUsageException(override val message: String) : RuntimeException(message)
