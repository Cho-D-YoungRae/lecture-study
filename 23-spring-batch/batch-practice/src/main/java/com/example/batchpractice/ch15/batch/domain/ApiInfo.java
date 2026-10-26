package com.example.batchpractice.ch15.batch.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApiInfo {

    private String url;

    private List<? extends ApiRequestVO> apiRequests;
}
