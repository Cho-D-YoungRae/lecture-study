package com.example.batchapiservice;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    @PostMapping("/api/product/1")
    public String product1(@RequestBody ApiInfo apiInfo) {
        List<ProductVO> products = apiInfo.getApiRequests().stream()
                .map(ApiRequestVO::getProductVO)
                .collect(Collectors.toList());
        System.out.println("products = " + products);

        return "product1 was successfully proceed";
    }

    @PostMapping("/api/product/2")
    public String product2(@RequestBody ApiInfo apiInfo) {
        List<ProductVO> products = apiInfo.getApiRequests().stream()
                .map(ApiRequestVO::getProductVO)
                .collect(Collectors.toList());
        System.out.println("products = " + products);

        return "product2 was successfully proceed";
    }

    @PostMapping("/api/product/3")
    public String product3(@RequestBody ApiInfo apiInfo) {
        List<ProductVO> products = apiInfo.getApiRequests().stream()
                .map(ApiRequestVO::getProductVO)
                .collect(Collectors.toList());
        System.out.println("products = " + products);

        return "product3 was successfully proceed";
    }
}
