package com.example.methodref;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MethodRefEx4 {

    public static void main(String[] args) {
        List<Person> people = List.of(
                new Person("Kim"),
                new Person("Lee"),
                new Person("Park")
        );

        List<String> result1 = mapPersonToString(people, (Person p) -> p.introduce());
        System.out.println("result1 = " + result1);

        List<String> result2 = mapPersonToString(people, Person::introduce);
        System.out.println("result2 = " + result2);

        List<String> upperResult1 = mapStringToString(result1, (String s) -> s.toUpperCase());
        System.out.println("upperResult1 = " + upperResult1);

        List<String> upperResult2 = mapStringToString(result2, String::toUpperCase);
        System.out.println("upperResult2 = " + upperResult2);
    }

    static List<String> mapPersonToString(List<Person> people, Function<Person, String> fun) {
        List<String> result = new ArrayList<>();
        for (Person person : people) {
            result.add(fun.apply(person));
        }
        return result;
    }

    static List<String> mapStringToString(List<String> strings, Function<String, String> fun) {
        List<String> result = new ArrayList<>();
        for (String str : strings) {
            result.add(fun.apply(str));
        }
        return result;
    }
}
