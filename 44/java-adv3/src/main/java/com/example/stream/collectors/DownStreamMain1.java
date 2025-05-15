package com.example.stream.collectors;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DownStreamMain1 {

    public static void main(String[] args) {
        List<Student> students = List.of(
                new Student("Kim", 1, 85),
                new Student("Kim", 1, 85),
                new Student("Park", 1, 70),
                new Student("Lee", 2, 70),
                new Student("Han", 2, 90),
                new Student("Hoon", 3, 90),
                new Student("Ha", 3, 89)
        );

        Map<Integer, List<Student>> collect1_1 = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade,
                        Collectors.toList()
                ));
        System.out.println("collect1_1 = " + collect1_1);

        Map<Integer, List<Student>> collect1_2 = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade
                ));
        System.out.println("collect1_2 = " + collect1_2);

        Map<Integer, List<String>> collect2 = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade,
                        Collectors.mapping(
                                Student::getName,
                                Collectors.toList()
                        )
                ));
        System.out.println("collect2 = " + collect2);

        Map<Integer, Long> collect3 = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade,
                        Collectors.counting()
                ));
        System.out.println("collect3 = " + collect3);

        Map<Integer, Double> collect4 = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade,
                        Collectors.averagingInt(Student::getScore)
                ));
        System.out.println("collect4 = " + collect4);
    }
}
