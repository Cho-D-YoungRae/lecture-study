package io.dodn.commerce.core.domain

import io.dodn.commerce.storage.db.core.CancelBalanceEntity
import io.dodn.commerce.storage.db.core.CancelBalanceRepository
import io.dodn.commerce.storage.db.core.OrderEntity
import io.dodn.commerce.storage.db.core.OrderItemEntity
import io.dodn.commerce.storage.db.core.OrderItemRepository
import io.dodn.commerce.storage.db.core.OrderRepository
import io.dodn.commerce.storage.db.core.PaymentEntity
import io.dodn.commerce.storage.db.core.PaymentRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal

class CancelCalculatorTest {
    private val orderRepository: OrderRepository = mockk()
    private val orderItemRepository: OrderItemRepository = mockk()
    private val paymentRepository: PaymentRepository = mockk()
    private val cancelBalanceRepository: CancelBalanceRepository = mockk()
    private val ownedCouponReader: OwnedCouponReader = mockk()

    private val cancelCalculator = CancelCalculator(
        orderRepository,
        orderItemRepository,
        paymentRepository,
        cancelBalanceRepository,
        ownedCouponReader,
    )

    private val orderKey = "ORDER-1"
    private val userId = 1L
    private val orderId = 100L
    private val paymentId = 200L

    @BeforeEach
    fun setUp() {
        // Default mocks
        every { orderRepository.findByOrderKeyAndStatus(any(), any()) } returns mockk<OrderEntity> {
            every { id } returns orderId
            every { totalPrice } returns BigDecimal.valueOf(30000)
        }
    }

    @Test
    @DisplayName("Case1 - Point 결제 상황에서의 부분 취소 계산")
    fun calculatePartialCase1() {
        // [Payment] orderAmount: 30,000, usedPoint: 15,000, paidAmount: 15,000
        val payment = createPayment(orderAmount = 30000, usedPoint = 15000, paidAmount = 15000)
        every { paymentRepository.findByOrderId(orderId) } returns payment

        val item1 = createOrderItem(id = 1L, unitPrice = 10000)
        val item2 = createOrderItem(id = 2L, unitPrice = 10000)
        val item3 = createOrderItem(id = 3L, unitPrice = 10000)
        every { orderItemRepository.findByIdOrNull(1L) } returns item1
        every { orderItemRepository.findByIdOrNull(2L) } returns item2
        every { orderItemRepository.findByIdOrNull(3L) } returns item3

        // 1. OrderItem-1-Cancel
        val balance1 = createCancelBalance(cancellablePaid = 15000, cancellablePoint = 15000)
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance1
        val result1 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 1L, 1L))
        assertThat(result1.paidAmount).isEqualByComparingTo(BigDecimal.valueOf(10000))
        assertThat(result1.pointAmount).isEqualByComparingTo(BigDecimal.ZERO)
        assertThat(result1.isRestoreCoupon).isFalse()

        // 2. OrderItem-2-Cancel (OrderItem-1이 취소된 상태라고 가정)
        val balance2 = createCancelBalance(
            cancellablePaid = 5000,
            cancellablePoint = 15000,
            cancelledPaid = 10000,
            cancelledPoint = 0,
        )
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance2
        val result2 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 2L, 1L))
        assertThat(result2.paidAmount).isEqualByComparingTo(BigDecimal.valueOf(5000))
        assertThat(result2.pointAmount).isEqualByComparingTo(BigDecimal.valueOf(5000))
        assertThat(result2.isRestoreCoupon).isFalse()

        // 3. OrderItem-3-Cancel (OrderItem-1, 2가 취소된 상태라고 가정)
        val balance3 = createCancelBalance(
            cancellablePaid = 0,
            cancellablePoint = 10000,
            cancelledPaid = 15000,
            cancelledPoint = 5000,
        )
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance3
        val result3 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 3L, 1L))
        assertThat(result3.paidAmount).isEqualByComparingTo(BigDecimal.ZERO)
        assertThat(result3.pointAmount).isEqualByComparingTo(BigDecimal.valueOf(10000))
        assertThat(result3.isRestoreCoupon).isFalse()
    }

    @Test
    @DisplayName("Case2 - 쿠폰 결제 상황에서의 부분 취소 계산")
    fun calculatePartialCase2() {
        // [Payment] orderAmount: 50,000, couponDiscount: 10,000, paidAmount: 40,000
        // [Coupon] minOrderAmount: 35,000, discount: 10,000
        val payment = createPayment(orderAmount = 50000, couponDiscount = 10000, paidAmount = 40000, ownedCouponId = 50L)
        every { paymentRepository.findByOrderId(orderId) } returns payment

        every { ownedCouponReader.getOwnedCoupon(50L) } returns mockk<OwnedCoupon> {
            every { coupon } returns mockk<Coupon> {
                every { minOrderAmount } returns BigDecimal.valueOf(35000)
                every { discount } returns BigDecimal.valueOf(10000)
            }
        }

        every { orderRepository.findByOrderKeyAndStatus(orderKey, any()) } returns mockk<OrderEntity> {
            every { id } returns orderId
            every { totalPrice } returns BigDecimal.valueOf(50000)
        }

        val items = (1L..5L).map { createOrderItem(id = it, unitPrice = 10000) }
        every { orderItemRepository.findByOrderId(orderId) } returns items
        every { orderItemRepository.findByIdOrNull(1L) } returns items[0]
        every { orderItemRepository.findByIdOrNull(2L) } returns items[1]
        every { orderItemRepository.findByIdOrNull(3L) } returns items[2]
        every { orderItemRepository.findByIdOrNull(4L) } returns items[3]
        every { orderItemRepository.findByIdOrNull(5L) } returns items[4]

        // 1. OrderItem-1-Cancel
        val balance1 = createCancelBalance(cancellablePaid = 40000, cancellableCoupon = 10000)
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance1
        val result1 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 1L, 1L))
        assertThat(result1.paidAmount).isEqualByComparingTo(BigDecimal.valueOf(10000))
        assertThat(result1.couponAmount).isEqualByComparingTo(BigDecimal.ZERO)
        assertThat(result1.isRestoreCoupon).isFalse()

        // 2. OrderItem-2-Cancel (OrderItem-1 취소 후 잔액 40,000. Item-2 취소하면 30,000 되어 Coupon Broken)
        val balance2 = createCancelBalance(
            cancellablePaid = 30000,
            cancellableCoupon = 10000,
            cancelledPaid = 10000,
            cancelledCoupon = 0,
        )
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance2
        val result2 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 2L, 1L))
        // 예제: cancelPaidAmount : 0, cancelCouponDiscountAmount : 10,000
        assertThat(result2.paidAmount).isEqualByComparingTo(BigDecimal.ZERO)
        assertThat(result2.couponAmount).isEqualByComparingTo(BigDecimal.valueOf(10000))
        assertThat(result2.isRestoreCoupon).isTrue()

        // 3. OrderItem-3-Cancel (Item-1, 2 취소 후)
        val balance3 = createCancelBalance(
            cancellablePaid = 30000,
            cancellableCoupon = 0,
            cancelledPaid = 10000,
            cancelledCoupon = 10000,
        )
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance3
        val result3 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 3L, 1L))
        assertThat(result3.paidAmount).isEqualByComparingTo(BigDecimal.valueOf(10000))
        assertThat(result3.couponAmount).isEqualByComparingTo(BigDecimal.ZERO)
        assertThat(result3.isRestoreCoupon).isFalse()

        // 4. OrderItem-4-Cancel (Item-1, 2, 3 취소 후)
        val balance4 = createCancelBalance(
            cancellablePaid = 20000,
            cancellableCoupon = 0,
            cancelledPaid = 20000,
            cancelledCoupon = 10000,
        )
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance4
        val result4 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 4L, 1L))
        assertThat(result4.paidAmount).isEqualByComparingTo(BigDecimal.valueOf(10000))
        assertThat(result4.couponAmount).isEqualByComparingTo(BigDecimal.ZERO)
        assertThat(result4.isRestoreCoupon).isFalse()

        // 5. OrderItem-5-Cancel (Item-1, 2, 3, 4 취소 후)
        val balance5 = createCancelBalance(
            cancellablePaid = 10000,
            cancellableCoupon = 0,
            cancelledPaid = 30000,
            cancelledCoupon = 10000,
        )
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance5
        val result5 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 5L, 1L))
        assertThat(result5.paidAmount).isEqualByComparingTo(BigDecimal.valueOf(10000))
        assertThat(result5.couponAmount).isEqualByComparingTo(BigDecimal.ZERO)
        assertThat(result5.isRestoreCoupon).isFalse()
    }

    @Test
    @DisplayName("Case3 - 복합 결제 상황에서의 부분 취소 계산")
    fun calculatePartialCase3() {
        // [Payment] orderAmount: 60,000, usedPoint: 15,000, couponDiscount: 15,000, paidAmount: 30,000
        // [Coupon] minOrderAmount: 35,000, discount: 15,000
        val payment = createPayment(orderAmount = 60000, usedPoint = 15000, couponDiscount = 15000, paidAmount = 30000, ownedCouponId = 50L)
        every { paymentRepository.findByOrderId(orderId) } returns payment

        every { ownedCouponReader.getOwnedCoupon(50L) } returns mockk<OwnedCoupon> {
            every { coupon } returns mockk<Coupon> {
                every { minOrderAmount } returns BigDecimal.valueOf(35000)
                every { discount } returns BigDecimal.valueOf(15000)
            }
        }

        every { orderRepository.findByOrderKeyAndStatus(orderKey, any()) } returns mockk<OrderEntity> {
            every { id } returns orderId
            every { totalPrice } returns BigDecimal.valueOf(60000)
        }

        val items = (1L..6L).map { createOrderItem(id = it, unitPrice = 10000) }
        every { orderItemRepository.findByIdOrNull(1L) } returns items[0]
        every { orderItemRepository.findByIdOrNull(2L) } returns items[1]
        every { orderItemRepository.findByIdOrNull(3L) } returns items[2]
        every { orderItemRepository.findByIdOrNull(4L) } returns items[3]
        every { orderItemRepository.findByIdOrNull(5L) } returns items[4]
        every { orderItemRepository.findByIdOrNull(6L) } returns items[5]

        // 1. OrderItem-1-Cancel
        val balance1 = createCancelBalance(cancellablePaid = 30000, cancellablePoint = 15000, cancellableCoupon = 15000)
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance1
        val result1 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 1L, 1L))
        assertThat(result1.paidAmount).isEqualByComparingTo(BigDecimal.valueOf(10000))
        assertThat(result1.isRestoreCoupon).isFalse()

        // 2. OrderItem-2-Cancel
        val balance2 = createCancelBalance(
            cancellablePaid = 20000,
            cancellablePoint = 15000,
            cancellableCoupon = 15000,
            cancelledPaid = 10000,
        )
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance2
        val result2 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 2L, 1L))
        assertThat(result2.paidAmount).isEqualByComparingTo(BigDecimal.valueOf(10000))
        assertThat(result2.isRestoreCoupon).isFalse()

        // 3. OrderItem-3-Cancel (잔액 40,000. Item-3 취소시 30,000 되어 Coupon Broken)
        // 예제: cancelPaidAmount : 0, cancelCouponDiscountAmount : 10,000
        val balance3 = createCancelBalance(
            cancellablePaid = 10000,
            cancellablePoint = 15000,
            cancellableCoupon = 15000,
            cancelledPaid = 20000,
        )
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance3
        val result3 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 3L, 1L))
        assertThat(result3.paidAmount).isEqualByComparingTo(BigDecimal.ZERO)
        assertThat(result3.couponAmount).isEqualByComparingTo(BigDecimal.valueOf(10000))
        assertThat(result3.isRestoreCoupon).isTrue()

        // 4. OrderItem-4-Cancel
        // 예제: cancelPaidAmount : 5,000, cancelCouponDiscountAmount : 5,000
        val balance4 = createCancelBalance(
            cancellablePaid = 10000,
            cancellablePoint = 15000,
            cancellableCoupon = 5000,
            cancelledPaid = 20000,
            cancelledCoupon = 10000,
        )
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance4
        val result4 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 4L, 1L))
        assertThat(result4.paidAmount).isEqualByComparingTo(BigDecimal.valueOf(5000))
        assertThat(result4.couponAmount).isEqualByComparingTo(BigDecimal.valueOf(5000))
        assertThat(result4.isRestoreCoupon).isFalse()

        // 5. OrderItem-5-Cancel
        // 예제: cancelPaidAmount : 5,000, cancelPointAmount : 5,000
        val balance5 = createCancelBalance(
            cancellablePaid = 5000,
            cancellablePoint = 15000,
            cancellableCoupon = 0,
            cancelledPaid = 25000,
            cancelledCoupon = 15000,
        )
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance5
        val result5 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 5L, 1L))
        assertThat(result5.paidAmount).isEqualByComparingTo(BigDecimal.valueOf(5000))
        assertThat(result5.pointAmount).isEqualByComparingTo(BigDecimal.valueOf(5000))
        assertThat(result5.isRestoreCoupon).isFalse()

        // 6. OrderItem-6-Cancel
        // 예제: cancelPaidAmount : 0, cancelPointAmount : 10,000
        val balance6 = createCancelBalance(
            cancellablePaid = 0,
            cancellablePoint = 10000,
            cancellableCoupon = 0,
            cancelledPaid = 30000,
            cancelledPoint = 5000,
            cancelledCoupon = 15000,
        )
        every { cancelBalanceRepository.findByOrderId(orderId) } returns balance6
        val result6 = cancelCalculator.calculatePartial(PartialCancelAction(orderKey, 6L, 1L))
        assertThat(result6.paidAmount).isEqualByComparingTo(BigDecimal.ZERO)
        assertThat(result6.pointAmount).isEqualByComparingTo(BigDecimal.valueOf(10000))
        assertThat(result6.isRestoreCoupon).isFalse()
    }

    private fun createPayment(
        orderAmount: Int,
        usedPoint: Int = 0,
        couponDiscount: Int = 0,
        paidAmount: Int,
        ownedCouponId: Long = 0,
    ) = mockk<PaymentEntity> {
        every { id } returns paymentId
        every { originAmount } returns BigDecimal.valueOf(orderAmount.toLong())
        every { this@mockk.usedPoint } returns BigDecimal.valueOf(usedPoint.toLong())
        every { this@mockk.couponDiscount } returns BigDecimal.valueOf(couponDiscount.toLong())
        every { this@mockk.paidAmount } returns BigDecimal.valueOf(paidAmount.toLong())
        every { hasAppliedCoupon() } returns (ownedCouponId > 0)
        every { this@mockk.ownedCouponId } returns ownedCouponId
        every { userId } returns this@CancelCalculatorTest.userId
    }

    private fun createOrderItem(id: Long, unitPrice: Int) = mockk<OrderItemEntity> {
        every { this@mockk.id } returns id
        every { this@mockk.unitPrice } returns BigDecimal.valueOf(unitPrice.toLong())
    }

    private fun createCancelBalance(
        cancellablePaid: Int = 0,
        cancellablePoint: Int = 0,
        cancellableCoupon: Int = 0,
        cancelledPaid: Int = 0,
        cancelledPoint: Int = 0,
        cancelledCoupon: Int = 0,
    ) = mockk<CancelBalanceEntity> {
        every { cancellablePaidAmount } returns BigDecimal.valueOf(cancellablePaid.toLong())
        every { cancellablePointAmount } returns BigDecimal.valueOf(cancellablePoint.toLong())
        every { cancellableCouponAmount } returns BigDecimal.valueOf(cancellableCoupon.toLong())
        every { cancelledPaidAmount } returns BigDecimal.valueOf(cancelledPaid.toLong())
        every { cancelledPointAmount } returns BigDecimal.valueOf(cancelledPoint.toLong())
        every { cancelledCouponAmount } returns BigDecimal.valueOf(cancelledCoupon.toLong())
        every { totalCanceledAmount() } returns cancelledPaidAmount + cancelledPointAmount + cancelledCouponAmount
    }
}
