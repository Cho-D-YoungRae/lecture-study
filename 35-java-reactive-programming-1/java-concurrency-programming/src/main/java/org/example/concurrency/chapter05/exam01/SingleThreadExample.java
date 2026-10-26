package org.example.concurrency.chapter05.exam01;

public class SingleThreadExample {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        int sum = 0;
        for (int i = 0; i < 1000 + 1; i++) {
            sum += i;
            try {
                Thread.sleep(1);
                // 싱글 스레드 어플리케이션이므로 예외 발생 시 프로그램이 종료됨
//                throw new RuntimeException("예외 발생");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("합계: " + sum);
        System.out.println("소요 시간: " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
