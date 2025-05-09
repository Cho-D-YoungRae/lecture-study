package com.example.lambda.ex1;

public class M1Before {

    public static void greetMorning() {
        System.out.println("=== 아침 ===");
        System.out.println("Good Morning!");
        System.out.println("=== 끝 ===");
    }

    public static void greetAfternoon() {
        System.out.println("=== 점심 ===");
        System.out.println("Good Afternoon!");
        System.out.println("=== 끝 ===");
    }

    public static void greetEvening() {
        System.out.println("=== 저녁 ===");
        System.out.println("Good Evening!");
        System.out.println("=== 끝 ===");
    }

    public static void main(String[] args) {
        greetMorning();
        greetAfternoon();
        greetEvening();
    }
}
