package com.example.lambda.lambda3;

public class GenericMain3 {

    public static void main(String[] args) {
        ObjectFunction upperCase  = new ObjectFunction() {
            @Override
            public Object apply(Object s) {
                return ((String) s).toUpperCase();
            }
        };
        String result1 = (String) upperCase.apply("hello");
        System.out.println(result1);

        ObjectFunction square = new ObjectFunction() {
            @Override
            public Object apply(Object i) {
                return (Integer) i * (Integer) i;
            }
        };
        Integer result2 = (Integer) square.apply(5);
        System.out.println(result2);
    }

    @FunctionalInterface
    interface ObjectFunction {
        Object apply(Object s);
    }
}
