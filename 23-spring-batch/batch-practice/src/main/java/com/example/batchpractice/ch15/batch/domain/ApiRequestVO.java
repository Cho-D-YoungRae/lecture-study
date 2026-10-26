package com.example.batchpractice.ch15.batch.domain;

import lombok.*;

@Data
@Builder
public class ApiRequestVO {

    private Long id;

    private ProductVO productVO;
}
