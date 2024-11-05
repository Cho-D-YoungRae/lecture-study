package reflection;

import java.lang.reflect.Constructor;

public class ConstructV1 {

    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> helloClass = Class.forName("reflection.data.BasicData");

        System.out.println("===== constructors() =====");
        Constructor[] constructors = helloClass.getConstructors();
        for (Constructor constructor : constructors) {
            System.out.println(constructor);
        }

        System.out.println();
        System.out.println("===== declaredConstructors() =====");
        Constructor[] declaredConstructors = helloClass.getDeclaredConstructors();
        for (Constructor declaredConstructor : declaredConstructors) {
            System.out.println(declaredConstructor);
        }
    }
}
