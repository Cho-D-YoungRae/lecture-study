package io.dodn.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface MerchantRepository : JpaRepository<MerchantEntity, Long>
