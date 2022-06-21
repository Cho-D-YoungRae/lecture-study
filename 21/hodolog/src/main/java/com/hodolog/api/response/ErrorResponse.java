package com.hodolog.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>(); // Map 사용을 안 하는 것으로 추후 개선 해보기

    public void addValidation(String field, String errorMessage) {
        validation.put(field, errorMessage);
    }
}
