package org.example.concurrency.chapter08.exam02;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockAPIExample {

    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock();

        Thread thread1 = new Thread(() -> {
            lock.lock();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        });

        Thread thread2 = new Thread(() -> {
            lock.lock();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        });

        thread1.start();
        thread2.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Hold Count: " + lock.getHoldCount());
        System.out.println("Is Held By Current Thread: " + lock.isHeldByCurrentThread());
        System.out.println("Has Queued Threads: " + lock.hasQueuedThreads());
        System.out.println("Has Queued Thread1: " + lock.hasQueuedThread(thread1));
        System.out.println("Has Queued Thread2: " + lock.hasQueuedThread(thread2));
        System.out.println("Queue Length: " + lock.getQueueLength());
        System.out.println("Is Fair: " + lock.isFair());
    }
}
