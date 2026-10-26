package org.example.concurrency.chapter02.exam02;

public class ThreadStartRunExample {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": 스레드 실행 중...");
        });
        // 메인 스레드가 먼저 종료될지, 생성된 스레드가 먼저 종료될지 모름
        // 어쨋든 둘다 종료되어야 어플리케이션이 종료
        thread.start();
        thread.run();
        System.out.println("메인 스레드 종료...");
    }
}
