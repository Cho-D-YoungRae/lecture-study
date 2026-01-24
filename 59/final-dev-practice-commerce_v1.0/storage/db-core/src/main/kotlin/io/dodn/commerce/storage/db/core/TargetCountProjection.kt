package io.dodn.commerce.storage.db.core

interface TargetCountProjection {
    fun getTargetId(): Long
    fun getCount(): Long
}
