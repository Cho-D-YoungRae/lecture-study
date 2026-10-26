package com.example.batchpractice.ch15.service;

import com.example.batchpractice.ch15.batch.domain.ApiInfo;
import com.example.batchpractice.ch15.batch.domain.ApiRequestVO;
import com.example.batchpractice.ch15.batch.domain.ApiResponseVO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

public abstract class AbstractApiService {

    public ApiResponseVO service(List<? extends ApiRequestVO> apiRequests) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .errorHandler(new ResponseErrorHandler() {
                    @Override
                    public boolean hasError(ClientHttpResponse response) throws IOException {
                        return false;
                    }

                    @Override
                    public void handleError(ClientHttpResponse response) throws IOException {

                    }
                }).build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ApiInfo apiInfo = ApiInfo.builder()
                .apiRequests(apiRequests)
                .build();

        return doApiService(restTemplate, apiInfo);
    }

    protected abstract ApiResponseVO doApiService(RestTemplate restTemplate, ApiInfo apiInfo);

}
