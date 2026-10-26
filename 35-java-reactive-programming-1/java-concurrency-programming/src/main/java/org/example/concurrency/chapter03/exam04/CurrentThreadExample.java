package org.example.concurrency.chapter03.exam04;

public class CurrentThreadExample {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("현재 스레드: " + Thread.currentThread());
        System.out.println("현재 스레드 이름: " + Thread.currentThread().getName());

        Thread thread = new Thread(() -> {
            System.out.println("현재 스레드: " + Thread.currentThread());
            System.out.println("현재 스레드 이름: " + Thread.currentThread().getName());
        });
        thread.start();
        thread.join();

        Thread threadRunnable = new Thread(new ThreadName());
        threadRunnable.start();
        threadRunnable.join();
    }

    static class ThreadName implements Runnable {
        @Override
        public void run() {
            System.out.println("현재 스레드: " + Thread.currentThread());
            System.out.println("현재 스레드 이름: " + Thread.currentThread().getName());
        }
    }
}
