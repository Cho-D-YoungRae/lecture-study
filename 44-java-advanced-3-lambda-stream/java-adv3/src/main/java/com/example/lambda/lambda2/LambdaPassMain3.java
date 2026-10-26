package com.example.lambda.lambda2;

import com.example.lambda.MyFunction;

public class LambdaPassMain3 {

    public static void main(String[] args) {
        MyFunction add = getOperation("add");
        System.out.println("add: " + add.apply(1, 2));

        MyFunction sub = getOperation("sub");
        System.out.println("sub: " + sub.apply(1, 2));

        MyFunction xxx = getOperation("xxx");
        System.out.println("xxx: " + xxx.apply(1, 2));
    }

    static MyFunction getOperation(String operator) {
        return switch (operator) {
            case "add" -> (a, b) -> a + b;
            case "sub" -> (a, b) -> a - b;
            default -> (a, b) -> 0;
        };
    }
}
