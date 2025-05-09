package com.example.lambda.lambda1;

import com.example.lambda.Procedure;

public class LambdaSimple2 {

    public static void main(String[] args) {
        Procedure procedure1 = () -> {
            System.out.println("Hello Lambda");
        };
        procedure1.run();

        // 단일 표현식인 경우 중괄호 생략 가능
        Procedure procedure2 = () -> System.out.println("Hello Lambda");
        procedure2.run();
    }
}
