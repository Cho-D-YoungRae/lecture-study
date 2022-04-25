package hello.proxy.jdkdynanmic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

@Slf4j
class ReflectionTest {

    @Test
    void reflection0() {
        Hello target = new Hello();

        // Common Logic 1 start
        log.info("start");
        String result1 = target.callA();    // 호출 대상이 다름
        log.info("result={}", result1);
        // Common Logic 1 finish

        // Common Logic 2 start
        log.info("start");
        String result2 = target.callB();    // 호출 대상이 다름
        log.info("result={}", result2);
        // Common Logic 2 finish
    }

    @Test
    void reflection1() throws Exception {
        // Class information
        Class<?> classHello = Class.forName("hello.proxy.jdkdynanmic.ReflectionTest$Hello");

        Hello target = new Hello();

        // callA method information
        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target);
        log.info("result1={}", result1);

        // callB method information
        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2={}", result2);
    }

    @Test
    void reflection2() throws Exception {
        // Class information
        Class<?> classHello = Class.forName("hello.proxy.jdkdynanmic.ReflectionTest$Hello");

        Hello target = new Hello();

        // callA method information
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        // callB method information
        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }

    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }

    @Slf4j
    static class Hello {

        public String callA() {
            log.info("Call A");
            return "A";
        }

        public String callB() {
            log.info("Call B");
            return "B";
        }
    }
}
