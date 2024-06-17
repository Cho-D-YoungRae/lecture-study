package org.example.concurrency.chapter03.exam03;

public class InterruptedExample1 {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (!Thread.interrupted()) {
                System.out.println(Thread.currentThread().getName() + ": 스레드 실행 중...");
            }
            System.out.println(Thread.currentThread().getName() + ": 스레드 인터럽트...");
            System.out.println(Thread.currentThread().getName() + " 인터럽트 상태: " + Thread.currentThread().isInterrupted());
        });
        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread.interrupt();
    }
}
