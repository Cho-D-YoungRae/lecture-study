package com.example.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;

    private Long price;

    private Long reservedQuantity;

    @Version
    private Long version;

    public Product(Long quantity, Long price, Long reservedQuantity) {
        this.quantity = quantity;
        this.price = price;
        this.reservedQuantity = reservedQuantity;
    }

    public Long reserve(Long requestedQuantity) {
        long reservableQuantity = this.quantity - this.reservedQuantity;

        if (reservableQuantity < requestedQuantity) {
            throw new IllegalStateException("예약할 수 있는 수량이 부족합니다.");
        }

        this.reservedQuantity += requestedQuantity;
        return this.price * requestedQuantity;
    }

    public Long calculatePrice(Long quantity) {
        return this.price * quantity;
    }

    public void buy(Long quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.quantity -= quantity;
    }
}
