package com.example.batchpractice.ch15.batch.chunk.writer;

import com.example.batchpractice.ch15.batch.domain.ApiRequestVO;
import com.example.batchpractice.ch15.batch.domain.ApiResponseVO;
import com.example.batchpractice.ch15.service.AbstractApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
public class ApiItemWriter3 implements ItemWriter<ApiRequestVO> {

    private final AbstractApiService apiService;

    @Override
    public void write(List<? extends ApiRequestVO> items) throws Exception {
        ApiResponseVO responseVO = apiService.service(items);
        System.out.println("responseVO = " + responseVO);
    }
}
