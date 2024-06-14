package org.example.concurrency.chapter03.exam01;

public class BasicSleepExample {

    public static void main(String[] args) {
        try {
            System.out.println("5초 후에 메시지가 출력됩니다");
            Thread.sleep(5000);
            System.out.println("Hello World");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
