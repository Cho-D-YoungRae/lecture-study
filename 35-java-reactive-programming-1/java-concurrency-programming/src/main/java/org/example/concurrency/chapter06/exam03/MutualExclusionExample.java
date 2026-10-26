package org.example.concurrency.chapter06.exam03;

public class MutualExclusionExample {

    private int count = 0;

    public synchronized void increment() {
        count += 1;
        System.out.println(Thread.currentThread().getName() + " : " + count);
    }

    public static void main(String[] args) throws InterruptedException {
        MutualExclusionExample example = new MutualExclusionExample();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                example.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                example.increment();
            }
        });

        thread1.start();
        thread2.start();
    }
}
