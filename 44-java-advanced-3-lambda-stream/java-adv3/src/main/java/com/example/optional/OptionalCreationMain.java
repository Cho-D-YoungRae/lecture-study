package com.example.optional;

import java.util.Optional;

public class OptionalCreationMain {

    public static void main(String[] args) {
        String nonNUllValue = "Hello Optional!";
        Optional<String> opt1 = Optional.of(nonNUllValue);
        System.out.println("opt1 = " + opt1);

        Optional<String> opt2 = Optional.ofNullable("Hello!");
        Optional<Object> opt3 = Optional.ofNullable(null);
        System.out.println("opt2 = " + opt2);
        System.out.println("opt3 = " + opt3);

        Optional<Object> opt4 = Optional.empty();
        System.out.println("opt4 = " + opt4);
    }
}
