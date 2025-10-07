package com.example.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_reservations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestId;

    private Long productId;

    private Long reservedQuantity;

    private Long reservedPrice;

    @Enumerated(EnumType.STRING)
    private ProductReservationStatus status;

    public ProductReservation(String requestId, Long productId, Long reservedQuantity, Long reservedPrice) {
        this.requestId = requestId;
        this.productId = productId;
        this.reservedQuantity = reservedQuantity;
        this.reservedPrice = reservedPrice;
        this.status = ProductReservationStatus.RESERVED;
    }

    public enum ProductReservationStatus {
        RESERVED,
        CONFIRMED,
        CANCELED,
        ;
    }
}
