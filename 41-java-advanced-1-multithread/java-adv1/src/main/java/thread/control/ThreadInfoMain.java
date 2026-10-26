package thread.control;

import thread.start.HelloRunnable;
import thread.start.HelloThread;

import static util.MyLogger.log;

public class ThreadInfoMain {

    public static void main(String[] args) {
        // main 스레드
        Thread mainThread = Thread.currentThread();
        log("mainThread=" + mainThread);
        log("mainThread.threadId()=" + mainThread.threadId());
        log("mainThread.getName()=" + mainThread.getName());
        log("mainThread.getPriority()=" + mainThread.getPriority());
        log("mainThread.getState()=" + mainThread.getState());
        log("mainThread.getThreadGroup()=" + mainThread.getThreadGroup());

        Thread myThread = new Thread(new HelloRunnable(), "myThread");
        log("myThread=" + myThread);
        log("myThread.threadId()=" + myThread.threadId());
        log("myThread.getName()=" + myThread.getName());
        log("myThread.getPriority()=" + myThread.getPriority());
        log("myThread.getState()=" + myThread.getState());
        log("myThread.getThreadGroup()=" + myThread.getThreadGroup());
    }
}
