package org.example.concurrency.chapter06.exam02;

public class SharedData {

    private int sharedValue = 0;

    private CommonSemaphore semaphore;

    public SharedData(final CommonSemaphore semaphore) {
        this.semaphore = semaphore;
    }

    public int getSharedValue() {
        return sharedValue;
    }

    public void sum() {
        try {
            semaphore.acquire();
            for (int i = 0; i < 10_000_000; i++) {
                sharedValue += 1;
            }
        } finally {
            semaphore.release();
        }
    }
}
