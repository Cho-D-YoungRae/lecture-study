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
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.payment.domain.PaymentEvent;
import org.example.payment.domain.PaymentMethod;
import org.example.payment.domain.PaymentType;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(
        name = "payment_events",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"order_id"}),
                @UniqueConstraint(columnNames = {"payment_key"})
        }
)
@NoArgsConstructor(access = PROTECTED)
@Getter
public class PaymentEventEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "is_payment_done", nullable = false)
    private Boolean paymentDone;

    @Nullable
    @Column(name = "payment_key")
    private String paymentKey;

    @Nullable
    @Column(name = "order_id")
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PaymentType type;

    @Column(name = "order_name", nullable = false)
    private String orderName;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private PaymentMethod method;

    @Nullable
    @Column(name = "psp_raw_data")
    private String pspRawData;

    @Nullable
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Builder
    public PaymentEventEntity(
            @Nullable final Long id,
            final Long buyerId,
            @Nullable final Boolean paymentDone,
            @Nullable final String paymentKey,
            @Nullable final String orderId,
            final PaymentType type,
            final String orderName,
            @Nullable final PaymentMethod method,
            @Nullable final String pspRawData,
            @Nullable final LocalDateTime approvedAt
    ) {
        this.id = id;
        this.buyerId = buyerId;
        this.paymentDone = paymentDone != null ? paymentDone : false;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.type = type;
        this.orderName = orderName;
        this.method = method;
        this.pspRawData = pspRawData;
        this.approvedAt = approvedAt;
    }

    public static PaymentEventEntity from(final PaymentEvent paymentEvent) {
        return new PaymentEventEntity(
                paymentEvent.id(),
                paymentEvent.buyerId(),
                paymentEvent.paymentDone(),
                paymentEvent.paymentKey(),
                paymentEvent.orderId(),
                paymentEvent.paymentType(),
                paymentEvent.orderName(),
                paymentEvent.paymentMethod(),
                null,
                paymentEvent.approvedAt()
        );
    }
}
