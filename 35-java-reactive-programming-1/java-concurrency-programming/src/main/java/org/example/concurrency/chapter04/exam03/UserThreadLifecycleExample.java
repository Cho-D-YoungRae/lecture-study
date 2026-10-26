package org.example.concurrency.chapter04.exam03;

public class UserThreadLifecycleExample {

    public static void main(String[] args) throws InterruptedException {

        final Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName() + " 실행 중");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println(Thread.currentThread().getName() + " 종료");
        });
        thread1.setName("사용자 스레드 1");

        final Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName() + " 실행 중");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println(Thread.currentThread().getName() + " 종료");
        });
        thread2.setName("사용자 스레드 2");

        thread1.start();
        thread2.start();

//        thread1.join();
//        thread2.join();

        System.out.println("모든 사용자 스레드가 종료되었습니다. 메인 스레드 종료.");
    }
}
