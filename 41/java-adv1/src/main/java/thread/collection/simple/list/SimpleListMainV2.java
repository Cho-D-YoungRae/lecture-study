package thread.collection.simple.list;

import static util.MyLogger.log;

public class SimpleListMainV2 {

    public static void main(String[] args) throws InterruptedException {
//        test(new BasicList());
//        test(new SyncList());
        test(new SyncProxyList(new BasicList()));
    }

    private static void test(SimpleList list) throws InterruptedException {
        log(list.getClass().getSimpleName());

        Thread thread1 = new Thread(() -> {
            list.add("A");
            log(Thread.currentThread().getName() + ": list.add(A)");
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            list.add("B");
            log(Thread.currentThread().getName() + ": list.add(B)");
        }, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        log(list);
    }
}
