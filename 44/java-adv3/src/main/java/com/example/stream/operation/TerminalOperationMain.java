package com.example.stream.operation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TerminalOperationMain {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 2, 3, 4, 5, 5, 6, 7, 8, 9, 10);

        System.out.println("collect");
        List<Integer> evenNumbers1 = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println("evenNumbers1 = " + evenNumbers1);
        System.out.println();

        System.out.println("toList");
        List<Integer> evenNumbers2 = numbers.stream()
                .filter(n -> n % 2 == 0)
                .toList();
        System.out.println("evenNumbers2 = " + evenNumbers2);
        System.out.println();

        System.out.println("toArray");
        Integer[] arr = numbers.stream()
                .filter(n -> n % 2 == 0)
                .toArray(Integer[]::new);
        System.out.print("arr = " + Arrays.toString(arr));
        System.out.println();

        System.out.println("forEach");
        numbers.stream()
                .limit(5)
                .forEach(n -> System.out.print(n + " "));
        System.out.println("\n");

        System.out.println("count");
        long count = numbers.stream()
                .filter(n -> n > 5)
                .count();
        System.out.println("count = " + count);
        System.out.println();

        System.out.println("reduce");
        System.out.println("초기값 X");
        Optional<Integer> sum1 = numbers.stream()
                .reduce((a, b) -> a + b);
        System.out.println("sum1 = " + sum1.get());

        System.out.println("초기값 O");
        Integer sum2 = numbers.stream()
                .reduce(100, (a, b) -> a + b);
        System.out.println("sum2 = " + sum2);
        System.out.println();

        System.out.println("min");
        Optional<Integer> min = numbers.stream()
                .min(Integer::compareTo);
        System.out.println(min.get());
        System.out.println();

        System.out.println("max");
        Optional<Integer> max = numbers.stream()
                .max(Integer::compareTo);
        System.out.println(max.get());
        System.out.println();

        System.out.println("findFirst");
        Optional<Integer> first = numbers.stream()
                .filter(n -> n > 5)
                .findFirst();
        System.out.println("first = " + first.get());
        System.out.println();

        System.out.println("findAny");
        Optional<Integer> any = numbers.stream()
                .filter(n -> n > 5)
                .findAny();
        System.out.println("any = " + any.get());
        System.out.println();

        System.out.println("anyMatch");
        boolean hasEven = numbers.stream()
                .anyMatch(n -> n % 2 == 0);
        System.out.println("hasEven = " + hasEven);
        System.out.println();

        System.out.println("allMatch");
        boolean allPositive = numbers.stream()
                .allMatch(n -> n > 0);
        System.out.println("allPositive = " + allPositive);
        System.out.println();

        System.out.println("noneMatch");
        boolean noneNegative = numbers.stream()
                .noneMatch(n -> n < 0);
        System.out.println("noneNegative = " + noneNegative);
    }
}
