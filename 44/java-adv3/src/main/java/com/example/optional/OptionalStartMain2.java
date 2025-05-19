package com.example.optional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OptionalStartMain2 {

    private static final Map<Long, String> map = new HashMap<>();

    static {
        map.put(1L, "Kim");
        map.put(2L, "Seo");
    }

    public static void main(String[] args) {
        findAndPrint(1L);   // 값이 있는 경우
        findAndPrint(3L);   // 값이 없는 경우
    }

    // 이름이 있으면 이름을 대문자로 출력, 없으면 UNKNOWN
    static void findAndPrint(Long id) {
        String name = findNameById(id).orElse("UNKNOWN");
        System.out.println(id + ": " + name.toUpperCase());
    }

    static Optional<String> findNameById(Long id) {
        String foundName = map.get(id);
        return Optional.ofNullable(foundName);
    }
}
