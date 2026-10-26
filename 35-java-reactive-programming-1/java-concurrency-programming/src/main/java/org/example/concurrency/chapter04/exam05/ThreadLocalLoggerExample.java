package org.example.concurrency.chapter04.exam05;

public class ThreadLocalLoggerExample {

    public static void main(String[] args) throws InterruptedException {
        final Thread thread1 = new Thread(new LogWorker());
        final Thread thread2 = new Thread(new LogWorker());
        final Thread thread3 = new Thread(new LogWorker());

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
