package org.example.concurrency.chapter06.exam01;

public class SharedData {

    private int sharedValue = 0;

    private Mutex mutex;

    public SharedData(final Mutex mutex) {
        this.mutex = mutex;
    }

    public int getSharedValue() {
        return sharedValue;
    }

    public void sum() {
        try {
            mutex.acquire();
            for (int i = 0; i < 10_000_000; i++) {
                sharedValue += 1;
            }
        } finally {
            mutex.release();
        }
    }
}
