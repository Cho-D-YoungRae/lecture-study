package org.example.concurrency.chapter04.exam03;

public class DaemonThreadLifecycleExample {

    public static void main(String[] args) throws InterruptedException {
        Thread userThread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 실행 중");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " 종료");
        });
        userThread.setName("사용자 스레드");

        Thread daemonThread = new Thread(() -> {
            while (true) {
                System.out.println(Thread.currentThread().getName() + " 실행 중");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        daemonThread.setName("데몬 스레드");
        daemonThread.setDaemon(true);

        userThread.start();
        daemonThread.start();

        userThread.join();

        System.out.println("모든 사용자 스레드가 종료되었습니다. 메인 스레드 종료.");
    }
}
