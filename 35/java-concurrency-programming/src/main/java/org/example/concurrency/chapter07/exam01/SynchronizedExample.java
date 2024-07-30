package org.example.concurrency.chapter07.exam01;

public class SynchronizedExample {

    private int instanceCount = 0;

    private static int staticCount = 0;

    public synchronized void instanceMethod() {
        instanceCount++;
        System.out.println("인스턴스 메서드 동기화: " + instanceCount);
    }

    public static synchronized void staticMethod() {
        staticCount++;
        System.out.println("정적 메서드 동기화: " + staticCount);
    }

    public void instanceBlock() {
        synchronized (this) {
            instanceCount++;
            System.out.println("인스턴스 블록 동기화: " + instanceCount);
        }
    }

    public static void staticBlock() {
        synchronized (SynchronizedExample.class) {
            staticCount++;
            System.out.println("정적 블록 동기화: " + staticCount);
        }
    }

    public static void main(String[] args) {
        SynchronizedExample example = new SynchronizedExample();

        new Thread(example::instanceMethod).start();
        new Thread(example::instanceBlock).start();
        new Thread(SynchronizedExample::staticMethod).start();
        new Thread(SynchronizedExample::staticBlock).start();
    }
}
