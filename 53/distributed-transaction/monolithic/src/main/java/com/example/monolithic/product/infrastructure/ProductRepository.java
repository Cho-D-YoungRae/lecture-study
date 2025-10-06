package com.example.monolithic.product.infrastructure;

import com.example.monolithic.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
