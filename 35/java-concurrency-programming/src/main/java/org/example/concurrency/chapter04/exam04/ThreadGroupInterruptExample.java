package org.example.concurrency.chapter04.exam04;

public class ThreadGroupInterruptExample {

    public static void main(String[] args) throws InterruptedException {
        ThreadGroup topGroup = new ThreadGroup("상위그룹");
        ThreadGroup subGroup = new ThreadGroup(topGroup, "하위그룹");

        Thread 상위_그룹_스레드 = new Thread(topGroup, () -> {
            while (true) {
                System.out.println("상위 스레드 그룹 실행중...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "상위 그룹 스레드");

        Thread 하위_그룹_스레드 = new Thread(topGroup, () -> {
            while (true) {
                System.out.println("하위 스레드 그룹 실행중...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "하위 그룹 스레드");

        상위_그룹_스레드.start();
        하위_그룹_스레드.start();

        Thread.sleep(3000);

        System.out.println("스레드 그룹 중지");
        상위_그룹_스레드.interrupt();
        하위_그룹_스레드.interrupt();
    }
}
