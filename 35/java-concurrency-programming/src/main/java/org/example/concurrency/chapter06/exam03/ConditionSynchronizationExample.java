package org.example.concurrency.chapter06.exam03;

public class ConditionSynchronizationExample {

    private boolean available = false;

    public synchronized void produce() {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(Thread.currentThread().getName() + " : produced");
        available = true;
        notify();
    }

    public synchronized void consume() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(Thread.currentThread().getName() + " : consumed");
        available = false;
        notify();
    }

    public static void main(String[] args) throws InterruptedException {

        ConditionSynchronizationExample example = new ConditionSynchronizationExample();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                example.produce();
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                example.consume();
            }
        }).start();
    }
}
