package com.hodolog.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class HodologException extends RuntimeException {

    private final Map<String, String> validations = new HashMap<>();

    protected HodologException(String message) {
        super(message);
    }

    protected HodologException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String fieldMessage) {
        validations.put(fieldName, fieldMessage);
    }
}
