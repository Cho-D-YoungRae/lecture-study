package com.example.product.init;

import com.example.product.domain.Product;
import com.example.product.infrastructure.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataCreator {

    private final ProductRepository productRepository;


    @PostConstruct
    public void createTestData() {
        productRepository.save(new Product(100L, 100L, 0L));
    }
}
