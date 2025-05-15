package com.example.stream.collectors;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toUnmodifiableList;

public class Collectors1Basic {

    public static void main(String[] args) {
        List<String> list = Stream.of("Java", "Spring", "JPA")
                .collect(toList());
        System.out.println("list = " + list);

        List<String> unmodifiableList = Stream.of("Java", "Spring", "JPA")
                .collect(toUnmodifiableList());
        System.out.println("unmodifiableList = " + unmodifiableList);

        Set<Integer> set = Stream.of(1, 2, 2, 3, 3, 3)
                .collect(toSet());
        System.out.println("set = " + set);

        TreeSet<Integer> treeSet = Stream.of(3, 4, 5, 2, 1)
                .collect(toCollection(TreeSet::new));
        System.out.println("treeSet = " + treeSet);
    }
}
