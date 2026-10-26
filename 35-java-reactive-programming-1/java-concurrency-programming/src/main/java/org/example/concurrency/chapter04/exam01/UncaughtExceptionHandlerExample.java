package org.example.concurrency.chapter04.exam01;


public class UncaughtExceptionHandlerExample {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 시작!");

            throw new RuntimeException(Thread.currentThread().getName() + " 예외 발생");
        });

        thread1.setUncaughtExceptionHandler((t, e) -> {
            System.out.println(t.getName() + " 스레드에서 예외 발생: " + e);
        });

        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 시작!");

            throw new RuntimeException(Thread.currentThread().getName() + " 예외 발생");
        });

        thread2.setUncaughtExceptionHandler((t, e) -> {
            System.out.println(t.getName() + " 예외 발생 삐용삐용 " + e);
        });

        thread1.start();
        thread2.start();
    }
}
