package com.example.lambda.lambda1;

import com.example.lambda.Procedure;

public class ProcedureMain2 {

    public static void main(String[] args) {
        Procedure procedure = () -> System.out.println("Hello Lambda");

        procedure.run();
    }
}
