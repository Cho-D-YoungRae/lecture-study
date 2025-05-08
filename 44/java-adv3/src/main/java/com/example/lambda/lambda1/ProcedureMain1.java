package com.example.lambda.lambda1;

import com.example.lambda.Procedure;

public class ProcedureMain1 {

    public static void main(String[] args) {
        Procedure procedure = new Procedure() {
            @Override
            public void run() {
                System.out.println("Hello Lambda");
            }
        };

        procedure.run();
    }
}
