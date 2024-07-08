package org.example.concurrency.chapter06.exam01;

public class Mutex {

    private boolean lock = false;

    public synchronized void acquire() {
        while (lock) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.lock = true;
    }

    public synchronized void release() {
        this.lock = false;
        notify();
    }
}
