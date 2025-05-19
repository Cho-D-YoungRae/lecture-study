package com.example.optional;

import java.util.HashMap;
import java.util.Map;

public class OptionalStartMain1 {

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
        String name = findNameById(id);
        // 1. NullPointerException
        // System.out.println("name=" + name.toUpperCase());

        // 2. if 문을 활용한 null 체크
        if (name != null) {
            System.out.println(id + ": " + name.toUpperCase());
        } else {
            System.out.println(id + ": " + "UNKNOWN");
        }
    }

    static String findNameById(Long id) {
        return map.get(id);
    }
}
