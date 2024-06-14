package org.example.concurrency.chapter03.exam02;

public class InterruptedJoinExample {

    public static void main(String[] args) {
        final Thread mainThread = Thread.currentThread();

        Thread longRunningThread = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + ": 실행 중... - " + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                mainThread.interrupt();
                System.out.println(Thread.currentThread().getName() + ": 인터럽트 발생...");
            }
        });
        longRunningThread.setName("긴 작업 스레드");

        longRunningThread.start();

        Thread interrunptingThread = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + ": 5초 후에 인터럽트를 발생시킵니다.");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            longRunningThread.interrupt();
        });
        interrunptingThread.setName("인터럽트 스레드");
        interrunptingThread.start();

        try {
            System.out.println(Thread.currentThread().getName() + ": 다른 스레드의 완료를 기다립니다.");
            longRunningThread.join();
            System.out.println(Thread.currentThread().getName() + ": 작업 완료.");
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + ": 인터럽트 발생...");
            throw new RuntimeException(e);
        }
    }
}
