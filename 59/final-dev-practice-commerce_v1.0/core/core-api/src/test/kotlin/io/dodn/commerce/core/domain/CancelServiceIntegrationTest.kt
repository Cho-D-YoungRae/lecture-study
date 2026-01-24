package io.dodn.commerce.core.domain

import io.dodn.commerce.ContextTest
import io.dodn.commerce.core.enums.CouponType
import io.dodn.commerce.core.enums.OrderState
import io.dodn.commerce.core.enums.OwnedCouponState
import io.dodn.commerce.core.enums.PaymentMethod
import io.dodn.commerce.core.enums.PaymentState
import io.dodn.commerce.core.enums.TransactionType
import io.dodn.commerce.storage.db.core.CancelBalanceEntity
import io.dodn.commerce.storage.db.core.CancelBalanceRepository
import io.dodn.commerce.storage.db.core.CancelRepository
import io.dodn.commerce.storage.db.core.CouponEntity
import io.dodn.commerce.storage.db.core.CouponRepository
import io.dodn.commerce.storage.db.core.OrderEntity
import io.dodn.commerce.storage.db.core.OrderItemEntity
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.OrderRepository
import io.dodn.commerce.storage.db.core.OwnedCouponEntity
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import io.dodn.commerce.storage.db.core.PaymentEntity
import io.dodn.commerce.storage.db.core.PaymentRepository
import io.dodn.commerce.storage.db.core.PointBalanceEntity
import io.dodn.commerce.storage.db.core.PointBalanceRepository
import io.dodn.commerce.storage.db.core.TransactionHistoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class CancelServiceIntegrationTest(
    private val cancelService: CancelService,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val paymentRepository: PaymentRepository,
    private val pointBalanceRepository: PointBalanceRepository,
    private val couponRepository: CouponRepository,
    private val ownedCouponRepository: OwnedCouponRepository,
    private val cancelRepository: CancelRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val cancelBalanceRepository: CancelBalanceRepository,
) : ContextTest() {

    private val userId = 1L

    @AfterEach
    fun tearDown() {
        cancelRepository.deleteAll()
        transactionHistoryRepository.deleteAll()
        cancelBalanceRepository.deleteAll()
        paymentRepository.deleteAll()
        orderItemRepository.deleteAll()
        orderRepository.deleteAll()
        ownedCouponRepository.deleteAll()
        couponRepository.deleteAll()
        pointBalanceRepository.deleteAll()
    }

    @Test
    @DisplayName("Case1 - Point 결제 상황에서의 부분 취소 통합 테스트")
    fun partialCancelCase1() {
        // Given: [Payment] orderAmount: 30,000, usedPoint: 15,000, paidAmount: 15,000
        val order = createOrder("ORDER-CASE-1", BigDecimal.valueOf(30000))
        val items = (1..3).map {
            createOrderItem(order.id, "상품-$it", BigDecimal.valueOf(10000))
        }
        setupPointBalance(BigDecimal.valueOf(15000)) // 초기 포인트

        // 결제 완료 상태 시뮬레이션
        paymentRepository.save(
            PaymentEntity(
                userId = userId,
                orderId = order.id,
                originAmount = BigDecimal.valueOf(30000),
                ownedCouponId = 0,
                couponDiscount = BigDecimal.ZERO,
                usedPoint = BigDecimal.valueOf(15000),
                payerUserId = userId,
                paidAmount = BigDecimal.valueOf(15000),
                state = PaymentState.SUCCESS,
                externalPaymentKey = "PG-KEY-1",
                method = PaymentMethod.CARD,
                approveCode = "APPROVE-1",
                paidAt = LocalDateTime.now(),
            ),
        )
        cancelBalanceRepository.save(
            CancelBalanceEntity(
                orderId = order.id,
                cancellablePaidAmount = BigDecimal.valueOf(15000),
                cancellablePointAmount = BigDecimal.valueOf(15000),
                cancellableCouponAmount = BigDecimal.ZERO,
            ),
        )

        val user = User(userId)

        // When & Then: OrderItem-1-Cancel (10,000)
        // 기대값: cancelPaidAmount : 10,000, cancelPointAmount : 0
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[0].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.valueOf(10000),
            expectedPoint = BigDecimal.ZERO,
            expectedCoupon = BigDecimal.ZERO,
            remainPaid = BigDecimal.valueOf(5000),
            remainPoint = BigDecimal.valueOf(15000),
            remainCoupon = BigDecimal.ZERO,
        )

        // When & Then: OrderItem-2-Cancel (10,000)
        // 기대값: cancelPaidAmount : 5,000, cancelPointAmount : 5,000
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[1].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.valueOf(5000),
            expectedPoint = BigDecimal.valueOf(5000),
            expectedCoupon = BigDecimal.ZERO,
            remainPaid = BigDecimal.ZERO,
            remainPoint = BigDecimal.valueOf(10000),
            remainCoupon = BigDecimal.ZERO,
        )

        // When & Then: OrderItem-3-Cancel (10,000)
        // 기대값: cancelPaidAmount : 0, cancelPointAmount : 10,000
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[2].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.ZERO,
            expectedPoint = BigDecimal.valueOf(10000),
            expectedCoupon = BigDecimal.ZERO,
            remainPaid = BigDecimal.ZERO,
            remainPoint = BigDecimal.ZERO,
            remainCoupon = BigDecimal.ZERO,
        )

        // 최종 상태 확인
        val finalOrder = orderRepository.findById(order.id).get()
        assertThat(finalOrder.state).isEqualTo(OrderState.CANCELED)
    }

    @Test
    @DisplayName("Case2 - 쿠폰 결제 상황에서의 부분 취소 통합 테스트")
    fun partialCancelCase2() {
        setupPointBalance(BigDecimal.valueOf(0)) // 초기 포인트
        // Given: [Payment] orderAmount: 50,000, couponDiscount: 10,000, paidAmount: 40,000
        // [Coupon] minOrderAmount: 35,000, discount: 10,000
        val coupon = couponRepository.save(
            CouponEntity(
                name = "3.5만 이상 1만 할인",
                type = CouponType.FIXED_AMOUNT,
                discount = BigDecimal.valueOf(10000),
                maxUseCount = 1,
                minOrderAmount = BigDecimal.valueOf(35000),
                expiredAt = LocalDateTime.now().plusDays(7),
            ),
        )
        val ownedCoupon = ownedCouponRepository.save(
            OwnedCouponEntity(userId, coupon.id, OwnedCouponState.USED, 1, 1),
        )

        val order = createOrder("ORDER-CASE-2", BigDecimal.valueOf(50000))
        val items = (1..5).map {
            createOrderItem(order.id, "상품-$it", BigDecimal.valueOf(10000))
        }

        paymentRepository.save(
            PaymentEntity(
                userId = userId,
                orderId = order.id,
                originAmount = BigDecimal.valueOf(50000),
                ownedCouponId = ownedCoupon.id,
                couponDiscount = BigDecimal.valueOf(10000),
                usedPoint = BigDecimal.ZERO,
                payerUserId = userId,
                paidAmount = BigDecimal.valueOf(40000),
                state = PaymentState.SUCCESS,
                externalPaymentKey = "PG-KEY-2",
                method = PaymentMethod.CARD,
                approveCode = "APPROVE-2",
                paidAt = LocalDateTime.now(),
            ),
        )
        cancelBalanceRepository.save(
            CancelBalanceEntity(
                orderId = order.id,
                cancellablePaidAmount = BigDecimal.valueOf(40000),
                cancellablePointAmount = BigDecimal.ZERO,
                cancellableCouponAmount = BigDecimal.valueOf(10000),
            ),
        )

        val user = User(userId)

        // 1. OrderItem-1-Cancel (10,000) -> 잔액 40,000 >= 3.5만 (정상)
        // 기대값: cancelPaidAmount : 10,000, cancelCouponAmount : 0
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[0].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.valueOf(10000),
            expectedPoint = BigDecimal.ZERO,
            expectedCoupon = BigDecimal.ZERO,
            remainPaid = BigDecimal.valueOf(30000),
            remainPoint = BigDecimal.ZERO,
            remainCoupon = BigDecimal.valueOf(10000),
        )

        // 2. OrderItem-2-Cancel (10,000) -> 잔액 30,000 < 3.5만 (Broken)
        // 기대값: cancelPaidAmount : 0, cancelCouponAmount : 10,000
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[1].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.ZERO,
            expectedPoint = BigDecimal.ZERO,
            expectedCoupon = BigDecimal.valueOf(10000),
            remainPaid = BigDecimal.valueOf(30000),
            remainPoint = BigDecimal.ZERO,
            remainCoupon = BigDecimal.ZERO,
        )

        val updatedOwnedCoupon = ownedCouponRepository.findById(ownedCoupon.id).get()
        assertThat(updatedOwnedCoupon.state).isEqualTo(OwnedCouponState.DOWNLOADED) // 복원 확인

        // 3. OrderItem-3-Cancel (10,000)
        // 기대값: cancelPaidAmount : 10,000, cancelCouponAmount : 0
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[2].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.valueOf(10000),
            expectedPoint = BigDecimal.ZERO,
            expectedCoupon = BigDecimal.ZERO,
            remainPaid = BigDecimal.valueOf(20000),
            remainPoint = BigDecimal.ZERO,
            remainCoupon = BigDecimal.ZERO,
        )

        // 4. OrderItem-4-Cancel (10,000)
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[3].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.valueOf(10000),
            expectedPoint = BigDecimal.ZERO,
            expectedCoupon = BigDecimal.ZERO,
            remainPaid = BigDecimal.valueOf(10000),
            remainPoint = BigDecimal.ZERO,
            remainCoupon = BigDecimal.ZERO,
        )

        // 5. OrderItem-5-Cancel (10,000)
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[4].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.valueOf(10000),
            expectedPoint = BigDecimal.ZERO,
            expectedCoupon = BigDecimal.ZERO,
            remainPaid = BigDecimal.ZERO,
            remainPoint = BigDecimal.ZERO,
            remainCoupon = BigDecimal.ZERO,
        )
    }

    @Test
    @DisplayName("Case3 - 복합 결제 상황에서의 부분 취소 통합 테스트")
    fun partialCancelCase3() {
        // Given: [Payment] orderAmount: 60,000, usedPoint: 15,000, couponDiscount: 15,000, paidAmount: 30,000
        // [Coupon] minOrderAmount: 35,000, discount: 15,000
        val coupon = couponRepository.save(
            CouponEntity(
                name = "3.5만 이상 1.5만 할인",
                type = CouponType.FIXED_AMOUNT,
                discount = BigDecimal.valueOf(15000),
                maxUseCount = 1,
                minOrderAmount = BigDecimal.valueOf(35000),
                expiredAt = LocalDateTime.now().plusDays(7),
            ),
        )
        val ownedCoupon = ownedCouponRepository.save(
            OwnedCouponEntity(userId, coupon.id, OwnedCouponState.USED, 1, 1),
        )
        setupPointBalance(BigDecimal.valueOf(15000))

        val order = createOrder("ORDER-CASE-3", BigDecimal.valueOf(60000))
        val items = (1..6).map {
            createOrderItem(order.id, "상품-$it", BigDecimal.valueOf(10000))
        }

        paymentRepository.save(
            PaymentEntity(
                userId = userId,
                orderId = order.id,
                originAmount = BigDecimal.valueOf(60000),
                ownedCouponId = ownedCoupon.id,
                couponDiscount = BigDecimal.valueOf(15000),
                usedPoint = BigDecimal.valueOf(15000),
                payerUserId = userId,
                paidAmount = BigDecimal.valueOf(30000),
                state = PaymentState.SUCCESS,
                externalPaymentKey = "PG-KEY-3",
                method = PaymentMethod.CARD,
                approveCode = "APPROVE-3",
                paidAt = LocalDateTime.now(),
            ),
        )
        cancelBalanceRepository.save(
            CancelBalanceEntity(
                orderId = order.id,
                cancellablePaidAmount = BigDecimal.valueOf(30000),
                cancellablePointAmount = BigDecimal.valueOf(15000),
                cancellableCouponAmount = BigDecimal.valueOf(15000),
            ),
        )

        val user = User(userId)

        // 1. OrderItem-1-Cancel (10,000) -> 잔액 50,000 (정상)
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[0].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.valueOf(10000),
            expectedPoint = BigDecimal.ZERO,
            expectedCoupon = BigDecimal.ZERO,
            remainPaid = BigDecimal.valueOf(20000),
            remainPoint = BigDecimal.valueOf(15000),
            remainCoupon = BigDecimal.valueOf(15000),
        )

        // 2. OrderItem-2-Cancel (10,000) -> 잔액 40,000 (정상)
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[1].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.valueOf(10000),
            expectedPoint = BigDecimal.ZERO,
            expectedCoupon = BigDecimal.ZERO,
            remainPaid = BigDecimal.valueOf(10000),
            remainPoint = BigDecimal.valueOf(15000),
            remainCoupon = BigDecimal.valueOf(15000),
        )

        // 3. OrderItem-3-Cancel (10,000) -> 잔액 30,000 (Broken)
        // 기대값: cancelPaidAmount : 0, cancelCouponAmount : 10,000 (쿠폰 잔액 15,000 중 10,000 사용)
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[2].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.ZERO,
            expectedPoint = BigDecimal.ZERO,
            expectedCoupon = BigDecimal.valueOf(10000),
            remainPaid = BigDecimal.valueOf(10000),
            remainPoint = BigDecimal.valueOf(15000),
            remainCoupon = BigDecimal.valueOf(5000),
        )

        // 4. OrderItem-4-Cancel (10,000) -> Broken 상태 지속
        // 기대값: cancelPaidAmount : 5,000, cancelCouponAmount : 5,000 (쿠폰 잔액 5,000 털고, 결제금액 5,000 차감)
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[3].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.valueOf(5000),
            expectedPoint = BigDecimal.ZERO,
            expectedCoupon = BigDecimal.valueOf(5000),
            remainPaid = BigDecimal.valueOf(5000),
            remainPoint = BigDecimal.valueOf(15000),
            remainCoupon = BigDecimal.ZERO,
        )

        // 5. OrderItem-5-Cancel (10,000)
        // 기대값: cancelPaidAmount : 5,000, cancelPointAmount : 5,000
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[4].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.valueOf(5000),
            expectedPoint = BigDecimal.valueOf(5000),
            expectedCoupon = BigDecimal.ZERO,
            remainPaid = BigDecimal.ZERO,
            remainPoint = BigDecimal.valueOf(10000),
            remainCoupon = BigDecimal.ZERO,
        )

        // 6. OrderItem-6-Cancel (10,000)
        // 기대값: cancelPaidAmount : 0, cancelPointAmount : 10,000
        cancelService.partialCancel(user, PartialCancelAction(order.orderKey, items[5].id, 1L))
        verifyLastCancel(
            orderId = order.id,
            expectedPaid = BigDecimal.ZERO,
            expectedPoint = BigDecimal.valueOf(10000),
            expectedCoupon = BigDecimal.ZERO,
            remainPaid = BigDecimal.ZERO,
            remainPoint = BigDecimal.ZERO,
            remainCoupon = BigDecimal.ZERO,
        )
    }

    private fun createOrder(orderKey: String, totalPrice: BigDecimal): OrderEntity {
        return orderRepository.save(
            OrderEntity(
                userId = userId,
                orderKey = orderKey,
                name = "테스트 주문",
                totalPrice = totalPrice,
                state = OrderState.PAID,
            ),
        )
    }

    private fun createOrderItem(orderId: Long, name: String, totalPrice: BigDecimal): OrderItemEntity {
        return orderItemRepository.save(
            OrderItemEntity(
                orderId = orderId,
                productId = 1L,
                productOptionId = 1L,
                productName = name,
                productOptionName = "옵션",
                thumbnailUrl = "",
                shortDescription = "",
                quantity = 1,
                unitPrice = totalPrice,
                totalPrice = totalPrice,
                canceledQuantity = 0,
                state = OrderState.PAID,
            ),
        )
    }

    private fun setupPointBalance(balance: BigDecimal) {
        pointBalanceRepository.save(PointBalanceEntity(userId, balance))
    }

    private fun verifyLastCancel(
        orderId: Long,
        expectedPaid: BigDecimal,
        expectedPoint: BigDecimal,
        expectedCoupon: BigDecimal,
        remainPaid: BigDecimal,
        remainPoint: BigDecimal,
        remainCoupon: BigDecimal,
    ) {
        val lastCancel = cancelRepository.findAll().filter { it.orderId == orderId }.maxByOrNull { it.id }!!
        assertThat(lastCancel.canceledPaidAmount).isEqualByComparingTo(expectedPaid)
        assertThat(lastCancel.canceledPointAmount).isEqualByComparingTo(expectedPoint)
        assertThat(lastCancel.canceledCouponAmount).isEqualByComparingTo(expectedCoupon)

        val lastHistory = transactionHistoryRepository.findAll().filter { it.orderId == orderId }.maxByOrNull { it.id }!!
        assertThat(lastHistory.type).isEqualTo(TransactionType.PARTIAL_CANCEL)
        assertThat(lastHistory.paidAmount).isEqualByComparingTo(expectedPaid)
        assertThat(lastHistory.pointAmount).isEqualByComparingTo(expectedPoint)
        assertThat(lastHistory.couponAmount).isEqualByComparingTo(expectedCoupon)

        val balance = cancelBalanceRepository.findByOrderId(orderId)!!
        assertThat(balance.cancellablePaidAmount).isEqualByComparingTo(remainPaid)
        assertThat(balance.cancellablePointAmount).isEqualByComparingTo(remainPoint)
        assertThat(balance.cancellableCouponAmount).isEqualByComparingTo(remainCoupon)
    }
}
