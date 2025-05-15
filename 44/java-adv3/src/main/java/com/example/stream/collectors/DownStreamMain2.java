package com.example.stream.collectors;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DownStreamMain2 {

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

        Map<Integer, List<Student>> collect1 = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade
                ));
        System.out.println("collect1 = " + collect1);

        Map<Integer, Optional<Student>> collect2 = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade,
                        Collectors.reducing((s1, s2) -> s1.getScore() > s2.getScore() ? s1 : s2)
                ));
        System.out.println("collect2 = " + collect2);

        Map<Integer, Optional<Student>> collect3 = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade,
                        Collectors.maxBy(Comparator.comparingInt(Student::getScore))
                ));
        System.out.println("collect3 = " + collect3);

        Map<Integer, String> collect4 = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Student::getScore)),
                                sOpt -> sOpt.get().getName()
                        )
                ));
        System.out.println("collect4 = " + collect4);
    }
}
