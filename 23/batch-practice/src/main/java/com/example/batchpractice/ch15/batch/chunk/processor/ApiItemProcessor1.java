package com.example.batchpractice.ch15.batch.chunk.processor;

import com.example.batchpractice.ch15.batch.domain.ApiRequestVO;
import com.example.batchpractice.ch15.batch.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;

public class ApiItemProcessor1 implements ItemProcessor<ProductVO, ApiRequestVO> {

    @Override
    public ApiRequestVO process(ProductVO item) throws Exception {
        return ApiRequestVO.builder()
                .id(item.getId())
                .productVO(item)
                .build();
    }

}
