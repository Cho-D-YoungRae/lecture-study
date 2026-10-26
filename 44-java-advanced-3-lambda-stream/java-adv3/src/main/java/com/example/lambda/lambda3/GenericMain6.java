package com.example.lambda.lambda3;

public class GenericMain6 {

    public static void main(String[] args) {
        GenericFunction<String, String> toUpperCase = s -> s.toUpperCase();
        GenericFunction<String, Integer> stringLength = s -> s.length();
        GenericFunction<Integer, Integer> square = i -> i * i;
        GenericFunction<Integer, Boolean> isEven = i -> i % 2 == 0;

        System.out.println(toUpperCase.apply("hello"));
        System.out.println(stringLength.apply("hello"));
        System.out.println(square.apply(5));
        System.out.println(isEven.apply(5));
    }

    @FunctionalInterface
    interface GenericFunction<T, R> {
        R apply(T s);
    }
}
