package org.example.concurrency.chapter02.exam03;

public class TimedWaitingStateThreadExample {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        Thread.sleep(100);
        System.out.println("스레드 상태: " + thread.getState());
    }
}
