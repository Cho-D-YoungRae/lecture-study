package org.example.concurrency.chapter04.exam05;

public class InheritableThreadLocalExample {

    public static InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
//    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        threadLocal.set("부모 스레드 값");

        Thread childThread = new Thread(() -> {
            System.out.println("자식 스레드에서 부모로부터 상속받은 값: " + threadLocal.get());
            threadLocal.set("자식 스레드 값");
            System.out.println("자식 스레드 값 설정 후: " + threadLocal.get());
        });
        childThread.start();
        childThread.join();

        System.out.println("부모 스레드에서의 값: " + threadLocal.get());
    }
}
