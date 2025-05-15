package com.example.stream.basic;

import java.util.List;

public class LazyEvalMain1 {

    public static void main(String[] args) {
        List<Integer> data = List.of(1, 2, 3, 4, 5, 6);
        ex2(data);
    }

    private static void ex2(List<Integer> data) {
        List<Integer> result = data.stream()
                .filter(i -> {
                    boolean even = i % 2 == 0;
                    System.out.println("filter(): " + i + ", even = " + even);
                    return even;
                })
                .map(i -> {
                    int mapped = i * 10;
                    System.out.println("map(): " + i + ", mapped = " + mapped);
                    return mapped;
                })
                .toList();
        System.out.println("result = " + result);
    }
}
