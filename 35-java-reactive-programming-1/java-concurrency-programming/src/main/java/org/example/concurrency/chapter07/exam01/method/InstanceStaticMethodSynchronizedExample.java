package org.example.concurrency.chapter07.exam01.method;

public class InstanceStaticMethodSynchronizedExample {

    private static int staticCount = 0;

    private int instanceCount = 0;

    public static synchronized void incrementStaticCount() {
        staticCount++;
        System.out.println("[" + Thread.currentThread().getName() + "] increment: " + staticCount);
    }

    public synchronized void incrementInstanceCount() {
        instanceCount++;
        System.out.println("[" + Thread.currentThread().getName() + "] increment: " + instanceCount);
    }

    public int getInstanceCount() {
        return instanceCount;
    }

    public static int getStaticCount() {
        return staticCount;
    }

    public static void main(String[] args) throws InterruptedException {
        InstanceStaticMethodSynchronizedExample example = new InstanceStaticMethodSynchronizedExample();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                example.incrementInstanceCount();
            }
        }, "인스턴스 카운트 증가 스레드 - 1");

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                example.incrementInstanceCount();
            }
        }, "인스턴스 카운트 증가 스레드 - 2");

        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                incrementStaticCount();
            }
        }, "정적 카운트 증가 스레드 - 1");

        Thread thread4 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                incrementStaticCount();
            }
        }, "정적 카운트 증가 스레드 - 2");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();

        System.out.println("인스턴스 카운트: " + example.getInstanceCount());
        System.out.println("정적 카운트: " + getStaticCount());
    }
}
