package thread.executor.poolsize;

import thread.executor.RunnableTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class PoolSizeMainV3 {

    public static void main(String[] args) {
//        ExecutorService es = Executors.newCachedThreadPool();
        // 기본 설정이 1분인데 기다릴 수 없어서 아래와 같이 새로 생성
        ExecutorService es = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                3L, TimeUnit.SECONDS,
                new SynchronousQueue<>());

        log("pool 생성");
        printState(es);

        for (int i = 1; i <= 100; i++) {
            String taskName = "task-" + i;
            es.execute(new RunnableTask(taskName));
            printState(es, taskName);
        }

        sleep(3000);
        log("== 작업 수행 완료 ==");
        printState(es);

        sleep(3000);
        log("== maximumPoolSize 대기 시간 초과 ==");
        printState(es);

        es.close();
        log("== shutdown 완료 ==");
        printState(es);
    }
}
