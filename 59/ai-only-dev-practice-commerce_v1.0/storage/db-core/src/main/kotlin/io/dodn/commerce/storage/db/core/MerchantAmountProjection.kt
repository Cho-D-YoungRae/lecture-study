package io.dodn.commerce.storage.db.core

import java.math.BigDecimal

interface MerchantAmountProjection {
    fun getMerchantId(): Long
    fun getAmount(): BigDecimal
}
