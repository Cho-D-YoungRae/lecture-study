package org.example.concurrency.chapter07.exam01.method;

public class StaticMethodSynchronizedExample {

    private static int count = 0;

    public static synchronized void increment() {
        count++;
        System.out.println("[" + Thread.currentThread().getName() + "] increment: " + count);
    }

    public static synchronized void decrement() {
        count--;
        System.out.println("[" + Thread.currentThread().getName() + "] decrement: " + count);
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                increment();
                decrement();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                decrement();
                increment();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("count: " + count);
    }
}
