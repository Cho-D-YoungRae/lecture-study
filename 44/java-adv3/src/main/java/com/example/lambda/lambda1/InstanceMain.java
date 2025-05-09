package com.example.lambda.lambda1;

import com.example.lambda.Procedure;

public class InstanceMain {

    public static void main(String[] args) {
        Procedure procedure1 = new Procedure() {
            @Override
            public void run() {
                System.out.println("Hello Lambda");
            }
        };

        System.out.println("class.class: " + procedure1.getClass());
        System.out.println("class.instance: " + procedure1);

        System.out.println("=".repeat(50));

        Procedure procedure2 = () -> {
            System.out.println("Hello Lambda");
        };
        System.out.println("lambda.class: " + procedure2.getClass());
        System.out.println("lambda.instance: " + procedure2);
    }
}
