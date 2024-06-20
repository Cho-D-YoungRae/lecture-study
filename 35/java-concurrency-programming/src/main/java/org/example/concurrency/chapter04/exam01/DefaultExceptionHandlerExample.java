package org.example.concurrency.chapter04.exam01;

public class DefaultExceptionHandlerExample {

    public static void main(String[] args) throws InterruptedException {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.println(t.getName() + " 스레드에서 예외 발생: " + e);
        });

        Thread thread1 = new Thread(() -> {
            throw new RuntimeException(Thread.currentThread().getName() + " 예외 발생");
        });

        Thread thread2 = new Thread(() -> {
            throw new RuntimeException(Thread.currentThread().getName() + " 예외 발생");
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}
