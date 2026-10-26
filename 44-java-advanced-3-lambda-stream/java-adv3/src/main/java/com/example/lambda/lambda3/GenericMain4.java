package com.example.lambda.lambda3;

public class GenericMain4 {

    public static void main(String[] args) {
        GenericFunction<String, String> upperCase  = new GenericFunction<String, String>() {
            @Override
            public String apply(String s) {
                return s.toUpperCase();
            }
        };
        String result1 = upperCase.apply("hello");
        System.out.println(result1);

        GenericFunction<Integer, Integer> square = new GenericFunction<Integer, Integer>() {
            @Override
            public Integer apply(Integer i) {
                return i * i;
            }
        };
        Integer result2 = square.apply(5);
        System.out.println(result2);
    }

    @FunctionalInterface
    interface GenericFunction<T, R> {
        R apply(T s);
    }
}
