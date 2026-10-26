package com.example.parallel.forkjoin;

import com.example.parallel.HeavyJob;

import java.util.List;
import java.util.concurrent.RecursiveTask;

import static com.example.util.MyLogger.log;

public class SumTask extends RecursiveTask<Integer> {

//    private static final int THRESHHOLD = 4;
    private static final int THRESHHOLD = 2;

    private final List<Integer> list;

    public SumTask(List<Integer> list) {
        this.list = list;
    }

    @Override
    protected Integer compute() {
        // 작업 범위가 작으면 직접 계산
        if (list.size() <= THRESHHOLD) {
            log("[처리 시작] " + list);
            int sum = list.stream()
                    .mapToInt(HeavyJob::heavyTask)
                    .sum();
            log("[처리 완료] " + list + " -> sum: " + sum);
            return sum;
        } else {
            // 작업 범위가 크면 반으로 나누어 병렬 처리
            int mid = list.size() / 2;
            List<Integer> leftList = list.subList(0, mid);
            List<Integer> rightList = list.subList(mid, list.size());
            log("[분할] " + list + " -> LEFT" + leftList + ", RIGHT" + rightList);

            SumTask leftTask = new SumTask(leftList);
            SumTask rightTask = new SumTask(rightList);

            // 왼쪽 작업은 다른 스레드에서 처리
            leftTask.fork();
            // 오른쪽 작업은 현재 스레드에서 처리
            Integer rightResult = rightTask.compute();

            // 왼쪽 작업 결과를 기다림
            Integer leftResult = leftTask.join();
            int joinSum = leftResult + rightResult;
            log("LEFT[" + leftTask + "] + RIGHT[" + rightTask + "] -> sum: " + joinSum);
            return joinSum;
        }
    }
}
