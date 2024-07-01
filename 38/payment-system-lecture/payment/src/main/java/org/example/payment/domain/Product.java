package org.example.payment.domain;

public record Product(
        long id,
        long amount,
        int quantity,
        String name,
        long sellerId
) {
}
