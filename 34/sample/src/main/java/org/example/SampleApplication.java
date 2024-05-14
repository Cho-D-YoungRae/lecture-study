package org.example;

import java.util.Scanner;

public class SampleApplication {
    public static void main(String[] args) {
        final CalculationRequestReader calculationRequestReader = new CalculationRequestReader();
        final CalculationRequest calculationRequest = calculationRequestReader.read();

        final Calculator calculator = new Calculator();
        final long answer = calculator.calculate(
                calculationRequest.num1(),
                calculationRequest.operator(),
                calculationRequest.num2()
        );
        System.out.println("Answer: " + answer);
    }
}