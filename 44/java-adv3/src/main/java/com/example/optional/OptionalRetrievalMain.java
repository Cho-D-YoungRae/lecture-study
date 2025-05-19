package com.example.optional;

import java.util.Optional;

public class OptionalRetrievalMain {

    public static void main(String[] args) {
        Optional<String> optValue = Optional.of("Hello");
        Optional<String> optEmpty = Optional.empty();

        System.out.println("=== isPresent() / isEmpty()");
        System.out.println("optValue.isPresent() = " + optValue.isPresent());

        System.out.println("=== get() ===");
        System.out.println("optValue.get() = " + optValue.get());
        // optEmpty.get() // 예외 발생

        System.out.println("=== orElse() ===");
        System.out.println("optValue.orElse() = " + optValue.orElse("defaultValue"));
        System.out.println("optEmpty.orElse() = " + optEmpty.orElse("defaultValue"));

        System.out.println("=== orElseGet() ===");
        System.out.println("optValue.orElseGet() = " + optValue.orElseGet(() -> "defaultValue"));
        System.out.println("optEmpty.orElseGet() = " + optEmpty.orElseGet(() -> "defaultValue"));
    }
}
