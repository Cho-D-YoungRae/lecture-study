package com.example.methodref.start;

import java.util.function.BinaryOperator;

public class MethodRefStartV2 {

    public static void main(String[] args) {
        BinaryOperator<Integer> add1 = MethodRefStartV2::add;
        BinaryOperator<Integer> add2 = MethodRefStartV2::add;

        Integer result1 = add1.apply(10, 20);
        System.out.println("result1 = " + result1);

        Integer result2 = add2.apply(10, 20);
        System.out.println("result2 = " + result2);
    }

    static int add(int a, int b) {
        return a + b;
    }
}
