package com.example.methodref;

import java.util.function.Function;

public class MethodRefEx3 {

    public static void main(String[] args) {
        // 4. 임의 객체의 인스턴스 메서드 참조(특정 타입의)
        Person person1 = new Person("Kim");
        Person person2 = new Person("Lee");
        Person person3 = new Person("Park");

        Function<Person, String> fun1 = (Person person) -> person.introduce();
        System.out.println("person1.fun1 = " + fun1.apply(person1));
        System.out.println("person2.fun1 = " + fun1.apply(person1));
        System.out.println("person2.fun1 = " + fun1.apply(person1));

        Function<Person, String> fun2 = Person::introduce;
        System.out.println("person1.fun2 = " + fun2.apply(person1));
        System.out.println("person2.fun2 = " + fun2.apply(person2));
        System.out.println("person3.fun2 = " + fun2.apply(person3));
    }
}
