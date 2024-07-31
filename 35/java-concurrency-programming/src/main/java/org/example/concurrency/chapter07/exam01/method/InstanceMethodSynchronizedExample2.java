package org.example.concurrency.chapter07.exam01.method;

public class InstanceMethodSynchronizedExample2 {

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
        InstanceMethodSynchronizedExample2 example1 = new InstanceMethodSynchronizedExample2();
        InstanceMethodSynchronizedExample2 example2 = new InstanceMethodSynchronizedExample2();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                example1.increment();
                example2.decrement();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                example1.decrement();
                example2.increment();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("count1: " + example1.getCount());
        System.out.println("count2: " + example2.getCount());
    }
}
