package org.example;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super("Invalid input size, you must input 3 length");
    }
}
