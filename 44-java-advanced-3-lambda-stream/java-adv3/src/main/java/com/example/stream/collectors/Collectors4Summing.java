package com.example.stream.collectors;

import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Collectors4Summing {

    public static void main(String[] args) {
        Long count1 = Stream.of(1, 2, 3)
                .collect(Collectors.counting());
        System.out.println("count1 = " + count1);

        Long count2 = Stream.of(1, 2, 3)
                .count();
        System.out.println("count2 = " + count2);

        Double avg1 = Stream.of(1, 2, 3)
                .collect(Collectors.averagingInt(x -> x));
        System.out.println("avg1 = " + avg1);

        Double avg2 = Stream.of(1, 2, 3)
                .mapToInt(x -> x)
                .average()
                .getAsDouble();
        System.out.println("avg2 = " + avg2);

        double avg3 = IntStream.of(1, 2, 3)
                .average().getAsDouble();
        System.out.println("avg3 = " + avg3);

        IntSummaryStatistics stats = Stream.of("Apple", "Banana", "Tomato")
                .collect(Collectors.summarizingInt(String::length));
        System.out.println(stats.getCount());
        System.out.println(stats.getSum());
        System.out.println(stats.getMin());
        System.out.println(stats.getMax());
        System.out.println(stats.getAverage());
    }
}
