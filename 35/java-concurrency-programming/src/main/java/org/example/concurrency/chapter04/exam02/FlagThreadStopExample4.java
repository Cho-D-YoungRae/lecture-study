package org.example.concurrency.chapter04.exam02;

import java.util.concurrent.atomic.AtomicBoolean;

public class FlagThreadStopExample4 {

    private AtomicBoolean running = new AtomicBoolean(true);

    public static void main(String[] args) {
        new FlagThreadStopExample4().flagTest();
    }

    private void flagTest() {
        new Thread(() -> {
            int count = 0;
            while (running.get()) {
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
            running.set(false);
        }).start();
    }
}
