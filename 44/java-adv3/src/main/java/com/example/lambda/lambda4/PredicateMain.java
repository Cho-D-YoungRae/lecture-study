package com.example.lambda.lambda4;

import java.util.function.Predicate;

public class PredicateMain {

    public static void main(String[] args) {
        Predicate<Integer> predicate1 = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer % 2 == 0;
            }
        };
        System.out.println("predicate1: " + predicate1.test(10));

        Predicate<Integer> predicate2 = integer -> integer % 2 == 0;
        System.out.println("predicate2: " + predicate2.test(10));
    }
}
