package org.example.concurrency.chapter07.exam01.method;

public class InstanceMethodSynchronizedExample {

    private int count = 0;

    public synchronized void increment() {
        count++;
        System.out.println("[" + Thread.currentThread().getName() + "] increment: " + count);
    }

    public synchronized void decrement() {
        count--;
        System.out.println("[" + Thread.currentThread().getName() + "] decrement: " + count);
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        InstanceMethodSynchronizedExample example = new InstanceMethodSynchronizedExample();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                example.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                example.decrement();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("count: " + example.getCount());
    }
}
