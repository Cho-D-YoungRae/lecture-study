package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static util.MyLogger.log;

public class RejectMainV2 {

    public static void main(String[] args) {
        ExecutorService es = new ThreadPoolExecutor(
                1, 1, 0, TimeUnit.SECONDS, new SynchronousQueue<>(),
                new ThreadPoolExecutor.DiscardPolicy()
        );

        es.submit(new RunnableTask("task1"));
        es.submit(new RunnableTask("task2"));
        es.submit(new RunnableTask("task3"));

        es.close();
    }
}
