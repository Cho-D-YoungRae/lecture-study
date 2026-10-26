package com.example.stream.collectors;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.partitioningBy;

public class Collectors3Group {

    public static void main(String[] args) {
        List<String> names = List.of("Apple", "Avocado", "Banana", "Blueberry", "Cherry");
        Map<String, List<String>> grouped = names.stream()
                .collect(groupingBy(name -> name.substring(0, 1)));
        System.out.println("grouped = " + grouped);

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        Map<Boolean, List<Integer>> partitioned = numbers.stream()
                .collect(partitioningBy(n -> n % 2 == 0));
        System.out.println("partitioned = " + partitioned);
    }
}
