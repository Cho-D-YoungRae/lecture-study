package org.example.payment.adapter.out.persistent.repository;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.payment.domain.PaymentStatus;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "payment_orders")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class PaymentOrderEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "payment_event_id", nullable = false)
    private Long paymentEventId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_order_status", nullable = false)
    private PaymentStatus status;

    @Column(name = "ledger_updated", nullable = false)
    private Boolean ledgerUpdated;

    @Column(name = "wallet_updated", nullable = false)
    private Boolean walletUpdated;

    @Column(name = "failed_count", nullable = false)
    private Integer failedCount;

    @Column(name = "threshold_count", nullable = false)
    private Integer thresholdCount;

    public PaymentOrderEntity(
            @Nullable final Long id,
            final Long paymentEventId,
            final Long sellerId,
            final Long productId,
            final String orderId,
            final Long amount,
            @Nullable final PaymentStatus paymentOrderStatus,
            @Nullable final Boolean ledgerUpdated,
            @Nullable final Boolean walletUpdated,
            @Nullable final Integer failedCount,
            @Nullable final Integer thresholdCount
    ) {
        this.id = id;
        this.paymentEventId = paymentEventId;
        this.sellerId = sellerId;
        this.productId = productId;
        this.orderId = orderId;
        this.amount = amount;
        this.status = paymentOrderStatus != null ? paymentOrderStatus : PaymentStatus.NOT_STARTED;
        this.ledgerUpdated = ledgerUpdated != null ? ledgerUpdated : false;
        this.walletUpdated = walletUpdated != null ? walletUpdated : false;
        this.failedCount = failedCount != null ? failedCount : 0;
        this.thresholdCount = thresholdCount != null ? thresholdCount : 5;
    }
}
