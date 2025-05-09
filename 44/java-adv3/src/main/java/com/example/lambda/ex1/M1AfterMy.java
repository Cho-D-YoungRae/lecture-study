package com.example.lambda.ex1;

public class M1AfterMy {

    static void greet(String greeting) {
        System.out.println("=== 시작 ===");
        System.out.println(greeting);
        System.out.println("=== 끝 ===");

    }

    public static void main(String[] args) {
        greet("Good Morning!");
        greet("Good Afternoon!");
        greet("Good Evening!");
    }
}
