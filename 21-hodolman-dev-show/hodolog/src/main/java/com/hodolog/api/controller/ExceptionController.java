package com.hodolog.api.controller;

import com.hodolog.api.exception.HodologException;
import com.hodolog.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidExceptionHandler error", e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message("잘못된 요청입니다.")
                .build();
        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errorResponse;
    }

    @ExceptionHandler(HodologException.class)
    public ResponseEntity<ErrorResponse> hodologExceptionHandler(HodologException e) {
        log.error("hodologExceptionHandler error", e);
        int statusCode = e.getStatusCode();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        for (Map.Entry<String, String> entry : e.getValidations().entrySet()) {
            errorResponse.addValidation(entry.getKey(), entry.getValue());
        }

        return ResponseEntity
                .status(statusCode)
                .body(errorResponse);
    }
}
