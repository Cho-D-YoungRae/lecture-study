package com.example.functional;

import java.util.function.Function;

public class SideEffectMain1 {

    public static int count = 0;

    public static void main(String[] args) {
        System.out.println("before count = " + count);

        Function<Integer, Integer> func = x -> {
            count++;    // 부수 효과
            return x * 2;
        };
        func.apply(10);
        System.out.println("after count = " + count);
    }
}
