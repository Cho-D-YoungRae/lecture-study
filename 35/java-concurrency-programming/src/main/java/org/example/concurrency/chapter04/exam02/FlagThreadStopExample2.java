package org.example.concurrency.chapter04.exam02;

public class FlagThreadStopExample2 {

    private static volatile boolean running = true;

    public static void main(String[] args) {

        new Thread(() -> {
            int count = 0;
            while (running) {
                count++;
            }
            System.out.println(Thread.currentThread().getName() + " 종료");
            System.out.println("count: " + count);
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " 종료");
            running = false;
        }).start();

    }
}
