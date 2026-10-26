package com.example.functional;

import java.util.function.Function;

public class SideEffectMain2 {

    public static void main(String[] args) {
        Function<Integer, Integer> func = x -> {
            int result = x * 2;

            // 부수 효과
            System.out.println("x = " + x + ", result = " + result);
            return result;
        };
        func.apply(10);
    }
}
