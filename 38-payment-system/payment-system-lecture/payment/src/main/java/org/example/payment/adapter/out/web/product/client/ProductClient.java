package org.example.payment.adapter.out.web.product.client;

import org.example.payment.domain.Product;

import java.util.List;

public interface ProductClient {

    List<Product> getProducts(long cartId, List<Long> productIds);
}
