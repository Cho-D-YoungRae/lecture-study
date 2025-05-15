package com.example.stream;

import java.util.List;
import java.util.stream.Stream;

public class DuplicateExecutionMain {

    public static void main(String[] args) {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        stream.forEach(System.out::println);    // 1. 최초 실행

        // stream.forEach(System.out::println);    // 2. 스트림 중복 실행 X, 주석 풀고 실행하면 예외 발생

        // 대안: 대상 리스트를 스트림으로 새로 생성해서 사용
        List<Integer> list = List.of(1, 2, 3);
        Stream.of(list).forEach(System.out::println);
        Stream.of(list).forEach(System.out::println);
    }
}
