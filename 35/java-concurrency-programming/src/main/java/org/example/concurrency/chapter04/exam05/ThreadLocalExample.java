package org.example.concurrency.chapter04.exam05;

public class ThreadLocalExample {

//    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "Hello World");

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "의 threadLocal.get() 값: " + threadLocal.get());
            threadLocal.set(Thread.currentThread().getName() + "의 값");
            System.out.println(Thread.currentThread().getName() + "의 threadLocal.get() 값: " + threadLocal.get());
            threadLocal.remove();
            System.out.println(Thread.currentThread().getName() + "의 threadLocal.get() 값: " + threadLocal.get());
        }, "스레드1");
        thread1.start();
        thread1.join();


        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "의 threadLocal.get() 값: " + threadLocal.get());
            threadLocal.set(Thread.currentThread().getName() + "의 값");
            System.out.println(Thread.currentThread().getName() + "의 threadLocal.get() 값: " + threadLocal.get());
            threadLocal.remove();
            System.out.println(Thread.currentThread().getName() + "의 threadLocal.get() 값: " + threadLocal.get());
        }, "스레드2");
        thread2.start();
        thread2.join();

    }
}
