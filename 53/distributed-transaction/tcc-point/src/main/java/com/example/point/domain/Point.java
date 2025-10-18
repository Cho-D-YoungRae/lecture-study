package com.example.point.domain;

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
@Table(name = "points")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long amount;

    private Long reservedAmount;

    @Version
    private Long version;

    public Point(Long userId, Long amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public void reserve(Long reserveAmount) {
        long reservableAmount = this.amount - reserveAmount;

        if (reservableAmount < reserveAmount) {
            throw new IllegalArgumentException("예약 가능한 포인트가 부족합니다.");
        }

        this.reservedAmount += reserveAmount;
    }

    public void use(Long amount) {
        if (this.amount < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        this.amount -= amount;
    }
}
