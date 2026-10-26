package org.example.concurrency.chapter07.exam04;

public class DeadlockDynamicOrderExample {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            methodWithLocks(lock1, lock2);
        }).start();

        new Thread(() -> {
            methodWithLocks(lock2, lock1);
        }).start();
    }


    private static void methodWithLocks(Object lockA, Object lockB) {
        synchronized (lockA) {
            System.out.println(Thread.currentThread().getName() + ": " + lockA + " 획득");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName() + ": " + lockB + " 획득");
            }
        }
    }
}
