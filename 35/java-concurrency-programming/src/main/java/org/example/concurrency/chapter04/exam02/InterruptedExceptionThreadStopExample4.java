package org.example.concurrency.chapter04.exam02;

public class InterruptedExceptionThreadStopExample4 {

    public static void main(String[] args) {
        Thread worker = new Thread(() -> {
            try {
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " 실행 중");
                    System.out.println("인터럽트 상태 1: " + Thread.currentThread().isInterrupted());
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("인터럽트 상태 2: " + Thread.currentThread().isInterrupted());
            }
            System.out.println(Thread.currentThread().getName() + " 종료");
            System.out.println("인터럽트 상태 3: " + Thread.currentThread().isInterrupted());
        });
        worker.setName("worker");

        Thread stopper = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            worker.interrupt();
            System.out.println(Thread.currentThread().getName() + "가 " + worker.getName() + "를 인터럽트");
        });
        stopper.setName("stopper");

        worker.start();
        stopper.start();
    }
}
