package org.example.concurrency.chapter07.exam02;

import java.util.LinkedList;
import java.util.Queue;

class SharedQueue {

    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity = 5;
    private final Object lock = new Object();

    void produce(int item) throws InterruptedException {
        synchronized (lock) {

            while (queue.size() == capacity) {
                System.out.println("큐가 가득 찼습니다. 생산 중지...");
                lock.wait();
            }
            queue.add(item);
            System.out.println("생산: " + item);

            lock.notifyAll();
        }
    }

    void consumer() throws InterruptedException {
        synchronized (lock) {
            while (queue.isEmpty()) {
                System.out.println("큐가 비었습니다. 소비 중지...");
                lock.wait();
            }
            int item = queue.poll();
            System.out.println("소비: " + item);

            lock.notifyAll();
        }
    }

}

public class ProducerConsumerExample {

    public static void main(String[] args) {
        SharedQueue queue = new SharedQueue();
        int itemNum = 100;
        new Thread(() -> {
            for (int i = 0; i < itemNum; i++) {
                try {
                    queue.produce(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "생산 쓰레드").start();

        new Thread(() -> {
            for (int i = 0; i < itemNum; i++) {
                try {
                    queue.consumer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "소비 쓰레드").start();
    }
}
