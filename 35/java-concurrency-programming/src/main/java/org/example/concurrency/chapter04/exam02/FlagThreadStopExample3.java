package org.example.concurrency.chapter04.exam02;

/**
 * volatile 은 없으나 Thread.sleep(1) 을 추가하여 정상적으로 종료 완료
 * 스레드가 컨텍스트 스위칭이 되면서 cpu 캐시의 값을 비워줘야하고, 다시 시작할 때는 메모리에서 읽어와야 한다
 */
public class FlagThreadStopExample3 {

    private static boolean running = true;

    public static void main(String[] args) {

        new Thread(() -> {
            int count = 0;
            while (running) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                count++;
            }
            System.out.println(Thread.currentThread().getName() + " 종료");
            System.out.println("count: " + count);
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " 종료");
            running = false;
        }).start();

    }
}
