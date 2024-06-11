package org.example.concurrency.chapter01.exam02;

/**
 * 원래 OS 에서 스레드 스케줄링하고, 컨텍스트 스위칭도 하는 건데
 * 자바 코드로 그런 상세한 내용을 다룰 수는 없음.
 *
 * 자바 코드로 어느정도 컨텍스트스위칭을 설명할 수 있는 예제를 보여주는 것일뿐
 * 이것이 명확한 컨텍스트 스위칭이라고 할 수는 없음
 */

/**
 * 스레드간 컨텍스트 스위칭이 발생하기 떄문에 1, 2, 3 이 번갈아 가면서 실행됨
 */
public class ContextSwitchExample {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread 1: " + i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread 2: " + i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread 3: " + i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
