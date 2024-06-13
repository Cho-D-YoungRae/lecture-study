package org.example.concurrency.chapter02.exam03;

public class BlockedStateThreadExample {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                while (true) {

                }
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("락을 획득하려고 함");
            }
        });
        thread2.start();
        Thread.sleep(100);
        System.out.println("스레드 상태: " + thread2.getState());
    }
}
