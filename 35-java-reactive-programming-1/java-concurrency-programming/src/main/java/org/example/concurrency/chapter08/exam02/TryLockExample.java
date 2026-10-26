package org.example.concurrency.chapter08.exam02;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TryLockExample {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();

        Thread thread1 = new Thread(() -> {
            boolean acquired = false;
            while (!acquired) {
                acquired = lock.tryLock();
                if (acquired) {
                    System.out.println(Thread.currentThread().getName() + ": lock 획득");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        lock.unlock();
                        System.out.println(Thread.currentThread().getName() + ": lock 해제");
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + ": lock 획득 실패");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            boolean acquired = false;
            while (!acquired) {
                acquired = lock.tryLock();
                if (acquired) {
                    System.out.println(Thread.currentThread().getName() + ": lock 획득");
                    try {
                    } finally {
                        lock.unlock();
                        System.out.println(Thread.currentThread().getName() + ": lock 해제");
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + ": lock 획득 실패");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
