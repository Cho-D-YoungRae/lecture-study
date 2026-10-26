package org.example.concurrency.chapter08.exam02;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockInterruptiblyExample {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();

        Thread thread1 = new Thread(() -> {
            try {
                lock.lockInterruptibly();
                System.out.println(Thread.currentThread().getName() + ": lock 획득");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                } finally {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + ": lock 해제");
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ": interrupted");
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                lock.lockInterruptibly();
                System.out.println(Thread.currentThread().getName() + ": lock 획득");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                } finally {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + ": lock 해제");
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ": interrupted");
            }
        });

        thread1.start();
        thread2.start();

        thread1.interrupt();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
