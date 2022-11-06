package com.example.batchpractice.ch15.batch.chunk.processor;

import com.example.batchpractice.ch15.batch.domain.Product;
import com.example.batchpractice.ch15.batch.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor<ProductVO, Product> {

    @Override
    public Product process(ProductVO item) throws Exception {
        return Product.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .type(item.getType())
                .build();
    }
}
