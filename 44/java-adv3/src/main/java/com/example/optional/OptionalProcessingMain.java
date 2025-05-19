package com.example.optional;

import java.util.Optional;

public class OptionalProcessingMain {

    public static void main(String[] args) {
        Optional<String> optValue = Optional.of("Hello");
        Optional<String> optEmpty = Optional.empty();

        System.out.println("=== ifPresent() ===");
        optValue.ifPresent(v -> System.out.println("optValue: " + v));
        optEmpty.ifPresent(v -> System.out.println("optEmpty: " + v));

        System.out.println("=== ifPresentOrElse() ===");
        optValue.ifPresentOrElse(
            v -> System.out.println("optValue: " + v),
            () -> System.out.println("optValue: empty")
        );
        optEmpty.ifPresentOrElse(
            v -> System.out.println("optEmpty: " + v),
            () -> System.out.println("optEmpty: empty")
        );

        System.out.println("=== map() ===");
        System.out.println("optValue.map(String::length) = " + optValue.map(String::length));
        System.out.println("optEmpty.map(String::length) = " + optEmpty.map(String::length));

        System.out.println("=== flatMap() ===");
        Optional<String> flattenedOpt = optValue.flatMap(v -> Optional.of(v));
        System.out.println("optValue = " + optValue);
        System.out.println("flattenedOpt = " + flattenedOpt);

        System.out.println("=== filter() ===");
        System.out.println("optValue.filter(s -> s.startsWith(\"H\")) = " + optValue.filter(s -> s.startsWith("H")));
        System.out.println("optValue.filter(s -> s.startsWith(\"X\")) = " + optValue.filter(s -> s.startsWith("X")));

        System.out.println("=== stream() ===");
        optValue.stream().forEach(s -> System.out.println("optValue.stream(): " + s));
        optEmpty.stream().forEach(s -> System.out.println("optEmpty.stream(): " + s));

    }
}
