package org.example.concurrency.chapter06.exam04;

import java.util.concurrent.atomic.AtomicBoolean;

// spinlock과 synchronized의 성능 비교 -> 둘의 장단점을 비교해보기 위해
// 믿을만한 것은 아님 -> 학습용
public class SpinLockVsSynchronized {

    private AtomicBoolean spinLock = new AtomicBoolean(false);

    private final Object syncLock = new Object();

    private int count = 0;

    static final int THREAD_COUNT = 10_000;
    final int ITERATIONS = 10_000_000;

    public void useSpinLock() {
        while (!spinLock.compareAndSet(false, true));
        for (int i = 0; i < ITERATIONS; i++) {
            count++;
        }
        spinLock.set(false);
    }

    public void useSynchronized() {
        synchronized (syncLock) {
            for (int i = 0; i < ITERATIONS; i++) {
                count++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SpinLockVsSynchronized tester = new SpinLockVsSynchronized();

        // sychronized 성능 테스트
        Thread[] syncThreads = new Thread[THREAD_COUNT];
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < THREAD_COUNT; i++) {
            syncThreads[i] = new Thread(() -> {
                tester.useSynchronized();
            });
            syncThreads[i].start();
        }
        for (int i = 0; i < THREAD_COUNT; i++) {
            syncThreads[i].join();
        }
        long end1 = System.currentTimeMillis();
        System.out.println("synchronized 경과 시간: " + (end1 - start1) + "ms");

        // spinlock 성능 테스트
        Thread[] spinThreads = new Thread[THREAD_COUNT];
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < THREAD_COUNT; i++) {
            spinThreads[i] = new Thread(() -> {
                tester.useSpinLock();
            });
            spinThreads[i].start();
        }
        for (int i = 0; i < THREAD_COUNT; i++) {
            spinThreads[i].join();
        }
        long end2 = System.currentTimeMillis();
        System.out.println("spinlock 경과 시간: " + (end2 - start2) + "ms");
    }
}
