package org.example.concurrency.chapter07.exam04;

public class DeadlockOrderExample {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            process1();
        }).start();

        new Thread(() -> {
            process2();
        }).start();

    }

    private static void process1() {
        synchronized (lock1) {
            System.out.println(Thread.currentThread().getName() + ": lock1 획득");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + ": lock2 획득");
            }
        }
    }

    private static void process2() {
        synchronized (lock2) {
            System.out.println(Thread.currentThread().getName() + ": lock2 획득");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + ": lock1 획득");
            }
        }
    }
}
