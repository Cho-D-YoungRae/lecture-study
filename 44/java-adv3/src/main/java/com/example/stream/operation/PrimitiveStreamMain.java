package com.example.stream.operation;

import java.util.IntSummaryStatistics;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class PrimitiveStreamMain {

    public static void main(String[] args) {
        IntStream stream = IntStream.of(1, 2, 3, 4, 5);
        stream.forEach(i -> System.out.print(i + " "));
        System.out.println();

        IntStream range1 = IntStream.range(1, 6);
        IntStream range2 = IntStream.rangeClosed(1, 5);

        int sum = IntStream.range(1, 6).sum();
        System.out.println("sum = " + sum);

        double avg = IntStream.range(1, 6).average().getAsDouble();
        System.out.println("avg = " + avg);

        IntSummaryStatistics stats = IntStream.range(1, 6).summaryStatistics();
        System.out.println("count = " + stats.getCount());
        System.out.println("sum = " + stats.getSum());
        System.out.println("min = " + stats.getMin());
        System.out.println("max = " + stats.getMax());
        System.out.println("avg = " + stats.getAverage());

        LongStream longStream = IntStream.range(1, 6).asLongStream();
        DoubleStream doubleStream = IntStream.range(1, 6).asDoubleStream();
        Stream<Integer> boxed = IntStream.range(1, 6).boxed();

        LongStream mappedLong = IntStream.range(1, 6)
                .mapToLong(i -> i * 10L);

        DoubleStream mappedDouble = IntStream.range(1, 6)
                .mapToDouble(i -> i * 1.5);

        Stream<String> mappedObj = IntStream.range(1, 6)
                .mapToObj(i -> "Number: " + i);

        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5);
        IntStream intStream = integerStream.mapToInt(i -> i);

        int result = Stream.of(1, 2, 3, 4, 5)
                .mapToInt(i -> i)
                .sum();

        System.out.println("result = " + result);
    }
}
