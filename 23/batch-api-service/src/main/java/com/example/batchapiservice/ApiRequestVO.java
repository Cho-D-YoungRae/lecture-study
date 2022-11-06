package com.example.batchapiservice;

import lombok.*;

@Data
@Builder
public class ApiRequestVO {

    private Long id;

    private ProductVO productVO;
}
