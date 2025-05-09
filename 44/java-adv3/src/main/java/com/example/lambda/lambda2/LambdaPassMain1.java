package com.example.lambda.lambda2;

import com.example.lambda.MyFunction;

// 1. 람다를 변수에 대입하기
public class LambdaPassMain1 {

    public static void main(String[] args) {
        MyFunction add = (a, b) -> a + b;
        MyFunction sub = (a, b) -> a - b;

        System.out.println("add: " + add.apply(1, 2));
        System.out.println("sub: " + sub.apply(1, 2));

        MyFunction cal = add;
        System.out.println("cal: " + cal.apply(1, 2));

        cal = sub;
        System.out.println("cal: " + cal.apply(1, 2));
    }
}
