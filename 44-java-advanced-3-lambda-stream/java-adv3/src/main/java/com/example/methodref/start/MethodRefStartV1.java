package com.example.methodref.start;

import java.util.function.BinaryOperator;

public class MethodRefStartV1 {

    public static void main(String[] args) {
        BinaryOperator<Integer> add1 = (a, b) -> a + b;
        BinaryOperator<Integer> add2 = (a, b) -> a + b;

        Integer result1 = add1.apply(10, 20);
        System.out.println("result1 = " + result1);

        Integer result2 = add2.apply(10, 20);
        System.out.println("result2 = " + result2);
    }
}
