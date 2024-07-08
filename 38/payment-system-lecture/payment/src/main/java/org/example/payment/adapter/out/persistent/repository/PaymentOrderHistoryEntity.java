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
@Table(name = "payment_order_histories")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class PaymentOrderHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "payment_order_id", nullable = false)
    private Long paymentOrderId;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status")
    private PaymentStatus previousStatus;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status")
    private PaymentStatus newStatus;

    @Nullable
    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "reason")
    private String reason;

    public PaymentOrderHistoryEntity(
            @Nullable final String changedBy,
            @Nullable final Long id,
            @Nullable final PaymentStatus newStatus,
            final Long paymentOrderId,
            @Nullable final PaymentStatus previousStatus,
            @Nullable final String reason
    ) {
        this.changedBy = changedBy;
        this.id = id;
        this.newStatus = newStatus;
        this.paymentOrderId = paymentOrderId;
        this.previousStatus = previousStatus;
        this.reason = reason;
    }
}