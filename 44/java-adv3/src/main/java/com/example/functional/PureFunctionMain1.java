package com.example.functional;

import java.util.function.Function;

public class PureFunctionMain1 {

    public static void main(String[] args) {
        Function<Integer, Integer> func = x -> x * 2;
        System.out.println(func.apply(10));
        System.out.println(func.apply(10));
    }
}
