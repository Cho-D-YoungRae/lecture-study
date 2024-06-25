package org.example.concurrency.chapter05.exam02;

public class CpuNonSyncExample {

    private static int count = 0;

    private static final int ITERATIONS = 100_000;

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                count++;
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                count++;
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("예상 결과: " + (ITERATIONS * 2));
        System.out.println("실제 결과: " + count);
    }
}
