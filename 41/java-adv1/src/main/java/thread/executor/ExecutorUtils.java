package thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import static util.MyLogger.log;

public abstract class ExecutorUtils {

    public static void printState(ExecutorService executorService) {
        if (executorService instanceof ThreadPoolExecutor poolExecutor) {
            int pool = poolExecutor.getPoolSize();
            int active = poolExecutor.getActiveCount();
            int queued = poolExecutor.getQueue().size();
            long completedTask = poolExecutor.getCompletedTaskCount();
            log("[pool=" + pool + ", active=" + active + ", queued=" + queued + ", completedTask=" + completedTask + "]");
        } else {
            log(executorService);
        }
    }

    public static void printState(ExecutorService executorService, String taskName) {
        if (executorService instanceof ThreadPoolExecutor poolExecutor) {
            int pool = poolExecutor.getPoolSize();
            int active = poolExecutor.getActiveCount();
            int queued = poolExecutor.getQueue().size();
            long completedTask = poolExecutor.getCompletedTaskCount();
            log(taskName + " -> [pool=" + pool + ", active=" + active + ", queued=" + queued + ", completedTask=" + completedTask + "]");
        } else {
            log(executorService);
        }
    }
}
