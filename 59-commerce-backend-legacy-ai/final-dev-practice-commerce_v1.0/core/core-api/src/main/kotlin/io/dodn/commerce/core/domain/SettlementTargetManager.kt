package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CancelType
import io.dodn.commerce.core.enums.TransactionType
import io.dodn.commerce.storage.db.core.MerchantProductMappingRepository
import io.dodn.commerce.storage.db.core.OrderItemEntity
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.SettlementTargetEntity
import io.dodn.commerce.storage.db.core.SettlementTargetRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class SettlementTargetManager(
    private val settlementTargetRepository: SettlementTargetRepository,
    private val orderItemRepository: OrderItemRepository,
    private val merchantProductMappingRepository: MerchantProductMappingRepository,
) {
    @Transactional
    fun processPayments(
        settleDate: LocalDate,
        payments: List<SettlementPayment>,
    ) {
        val transactionIdMap = payments.associate { it.orderId to it.id }
        val orderItems: List<OrderItemEntity> = orderItemRepository.findByOrderIdIn(transactionIdMap.keys)

        val merchantMappingMap = merchantProductMappingRepository
            .findByProductIdIn(orderItems.map { it.productId }.toSet())
            .associateBy { it.productId }

        val targets = orderItems
            .filter { merchantMappingMap.containsKey(it.productId) }
            .map { item ->
                SettlementTargetEntity(
                    settlementDate = settleDate,
                    merchantId = merchantMappingMap[item.productId]?.merchantId ?: throw IllegalStateException("상품 ${item.productId} 의 가맹점 매핑이 존재하지 않음"),
                    transactionType = TransactionType.PAYMENT,
                    transactionId = transactionIdMap[item.orderId] ?: throw IllegalStateException("주문 ${item.orderId} 의 거래 ID 매핑이 존재하지 않음"),
                    orderId = item.orderId,
                    productId = item.productId,
                    quantity = item.quantity,
                    unitPrice = item.unitPrice,
                    totalPrice = item.totalPrice,
                    targetAmount = item.totalPrice,
                )
            }
        settlementTargetRepository.saveAll(targets)
    }

    @Transactional
    fun processCancels(
        settleDate: LocalDate,
        cancels: List<SettlementCancel>,
    ) {
        val allCancels = cancels.filter { it.type == CancelType.ALL }
        val cancelTargets = processAllCancels(settleDate, allCancels)

        val partialCancels = cancels.filter { it.type == CancelType.PARTIAL }
        val partialCancelTargets = processPartialCancels(settleDate, partialCancels)

        settlementTargetRepository.saveAll(cancelTargets + partialCancelTargets)
    }

    private fun processAllCancels(
        settleDate: LocalDate,
        cancels: List<SettlementCancel>,
    ): List<SettlementTargetEntity> {
        val transactionIdMap = cancels.associate { it.orderId to it.id }
        val orderItems = orderItemRepository.findByOrderIdIn(transactionIdMap.keys)

        val merchantMappingMap = merchantProductMappingRepository
            .findByProductIdIn(orderItems.map { it.productId }.toSet())
            .associateBy { it.productId }

        return orderItems
            .filter { merchantMappingMap.containsKey(it.productId) }
            .map { item ->
                SettlementTargetEntity(
                    settlementDate = settleDate,
                    merchantId = merchantMappingMap[item.productId]?.merchantId ?: throw IllegalStateException("상품 ${item.productId} 의 가맹점 매핑이 존재하지 않음"),
                    transactionType = TransactionType.CANCEL,
                    transactionId = transactionIdMap[item.orderId] ?: throw IllegalStateException("주문 ${item.orderId} 의 거래 ID 매핑이 존재하지 않음"),
                    orderId = item.orderId,
                    productId = item.productId,
                    quantity = item.quantity,
                    unitPrice = item.unitPrice,
                    totalPrice = item.totalPrice,
                    targetAmount = item.totalPrice.negate(),
                )
            }
    }

    private fun processPartialCancels(
        settleDate: LocalDate,
        cancels: List<SettlementCancel>,
    ): List<SettlementTargetEntity> {
        if (cancels.isEmpty()) return emptyList()

        val orderItemIds = cancels.map { it.orderItemId }.toSet()
        val orderItems = orderItemRepository.findAllById(orderItemIds)
        val orderItemsById = orderItems.associateBy { it.id }

        val productIds = orderItems.map { it.productId }.toSet()
        val merchantMappingMap = merchantProductMappingRepository
            .findByProductIdIn(productIds)
            .associateBy { it.productId }

        return cancels
            .filter { cancel ->
                val item = orderItemsById[cancel.orderItemId]
                item != null && merchantMappingMap.containsKey(item.productId)
            }
            .map { cancel ->
                val item = orderItemsById[cancel.orderItemId]!!
                val merchantMapping = merchantMappingMap[item.productId]!!

                val totalPrice = item.unitPrice.multiply((cancel.canceledQuantity).toBigDecimal())
                SettlementTargetEntity(
                    settlementDate = settleDate,
                    merchantId = merchantMapping.merchantId,
                    transactionType = TransactionType.PARTIAL_CANCEL,
                    transactionId = cancel.id,
                    orderId = item.orderId,
                    productId = item.productId,
                    quantity = cancel.canceledQuantity,
                    unitPrice = item.unitPrice,
                    totalPrice = totalPrice,
                    targetAmount = totalPrice.negate(),
                )
            }
    }
}
