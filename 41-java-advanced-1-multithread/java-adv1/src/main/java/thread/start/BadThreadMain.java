package thread.start;

public class BadThreadMain {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() start");

        HelloThread helloThread = new HelloThread();
        System.out.println(Thread.currentThread().getName() + ": start 호출 전");
        helloThread.run(); // run() 메소드를 직접 호출
        System.out.println(Thread.currentThread().getName() + ": start 호출 후");

        System.out.println(Thread.currentThread().getName() + ": main() end");
    }
}
