package com.example.lambda.ex1;

import com.example.lambda.Procedure;

import java.util.Arrays;

public class M4MeasureTimeMy {

    public static void measure(Procedure procedure) {
        long startNs = System.nanoTime();

        procedure.run();

        long endNs = System.nanoTime();
        System.out.println("실행 시간: " + (endNs - startNs) + "ns");
    }

    public static void main(String[] args) {

        measure(() -> {
            int n = 100;
            long sum = 0;
            for (int i = 1; i <= n; i++) {
                sum += i;
            }
            System.out.println("1부터 " + n + "까지의 합: " + sum);
        });

        measure(() -> {
            int[] arr = {4, 3, 2, 1};
            System.out.println("정렬 전: " + Arrays.toString(arr));
            Arrays.sort(arr);
            System.out.println("정렬 후: " + Arrays.toString(arr));
        });
    }
}
