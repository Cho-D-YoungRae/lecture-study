package org.example.concurrency.chapter05.exam03;

public class NoRaceConditionExample {

    private static int sharedResource = 0;

    public static void main(String[] args) {
        // 스레드 100개를 생성하여 공유 리소스를 동시에 증가시킴
        Thread[] incrementThreads = new Thread[100];
        for (int i = 0; i < incrementThreads.length; i++) {
            incrementThreads[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    sharedResource++;
                }
            });
            incrementThreads[i].start();
        }

        // 모든 스레드가 종료될 때까지 대기
        for (Thread thread : incrementThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 공유 리소스 값 출력
        System.out.println("실제 값 = " + sharedResource);
        System.out.println("예상 값 = " + 100 * 10000);
    }
}
