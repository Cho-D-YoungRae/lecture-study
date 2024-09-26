package org.example.concurrency.chapter08.exam01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockStateExample {

    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + ": lock 1번 획득");
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + ": lock 2번 획득");
                    lock.lock();
                    try {
                        System.out.println(Thread.currentThread().getName() + ": lock 3번 획득");
                    } finally {
                        lock.unlock();
                        System.out.println(Thread.currentThread().getName() + ": lock 1번 해제");
                    }
                } finally {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + ": lock 2번 해제");
                }
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + ": lock 3번 해제");
            }

        }).start();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + ": lock 획득");
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + ": lock 해제");
            }
        }).start();
    }
}
