package org.example.payment.application.port.out;

import org.example.payment.domain.Product;

import java.util.List;

public interface LoadProductPort {

    List<Product> getProducts(long cartId, List<Long> productIds);
}
