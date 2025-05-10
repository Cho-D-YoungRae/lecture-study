package com.example.lambda.lambda4;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class OperatorMain {

    public static void main(String[] args) {
        // UnaryOperator
        Function<Integer, Integer> square1 = x -> x * x;
        System.out.println("square1: " + square1.apply(5));
        UnaryOperator<Integer> square2 = x -> x * x;
        System.out.println("square2: " + square2.apply(5));

        // BinaryOperator
        BiFunction<Integer, Integer, Integer> add1 = (x, y) -> x + y;
        BinaryOperator<Integer> add2 = (x, y) -> x + y;
        System.out.println("add1: " + add1.apply(5, 10));
        System.out.println("add2: " + add2.apply(5, 10));

    }
}
