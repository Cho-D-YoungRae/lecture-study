package org.example.concurrency.chapter04.exam02;

public class IsInterruptedThreadStopExample {

    public static void main(String[] args) {
        Thread worker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread().getName() + " 실행 중");
            }
            System.out.println(Thread.currentThread().getName() + " 인터럽트 상태: " + Thread.currentThread().isInterrupted());
            System.out.println(Thread.currentThread().getName() + " 종료");
        });

        Thread stopper = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            worker.interrupt();
            System.out.println(Thread.currentThread().getName() + "가 " + worker.getName() + "를 인터럽트");
        });

        worker.start();
        stopper.start();
    }
}
