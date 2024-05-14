package org.example;

import java.util.Scanner;

public class CalculationRequestReader {

    public CalculationRequest read() {
        final Scanner scanner = new Scanner(System.in);
        System.out.print("Enter two numbers and an operator (e.g 1 + 2): ");
        final String result = scanner.nextLine();
        return CalculationRequest.of(result.split(" "));
    }
}
