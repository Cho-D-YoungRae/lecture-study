package com.example.lambda.lambda3;

public class GenericMain5 {

    public static void main(String[] args) {
        GenericFunction<String, String> upperCase  = s -> s.toUpperCase();
        String result1 = upperCase.apply("hello");
        System.out.println(result1);

        GenericFunction<Integer, Integer> square = i -> i * i;
        Integer result2 = square.apply(5);
        System.out.println(result2);
    }

    @FunctionalInterface
    interface GenericFunction<T, R> {
        R apply(T s);
    }
}
