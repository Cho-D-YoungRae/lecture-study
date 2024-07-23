package org.example.concurrency.chapter06.exam02;


public class CountingSemaphoreExample {

    public static void main(String[] args) throws InterruptedException {
        int permits = 10;
        SharedData sharedData = new SharedData(new CountingSemaphore(permits));

        int threadCount = 15;

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                sharedData.sum();
            });
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }

        System.out.println("sum = " + sharedData.getSharedValue());
    }
}
