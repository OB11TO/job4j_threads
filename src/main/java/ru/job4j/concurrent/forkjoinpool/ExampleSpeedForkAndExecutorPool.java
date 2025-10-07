package ru.job4j.concurrent.forkjoinpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class ExampleSpeedForkAndExecutorPool {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        try (ForkJoinPool forkJoinPool = ForkJoinPool.commonPool()) {
            forkJoinPool.submit(() -> System.out.println(
                    IntStream.range(0, 10000000).average().getAsDouble()
            )).get();

            executor();
        }
    }

    private static void executor() {
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            executor.submit(() -> System.out.println(
                    IntStream.range(0, 10000000).average().getAsDouble()
            ));
        }
    }

}
