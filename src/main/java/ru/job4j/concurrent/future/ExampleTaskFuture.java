package ru.job4j.concurrent.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExampleTaskFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        executorServiceWithoutTry();
        executorServiceWithTry();
        Runnable task = () -> System.out.println("Hello world");
        Callable<?> task2 = () -> "Hello World";
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(task);
        Future<?> future2 = executorService.submit(task2);
        Object o = future.get();
        Object o2 = future2.get();
        executorService.shutdown();
    }


    private static void executorServiceWithTry() {
        /*
                            Java 24 version
        try (ExecutorService executor = Executors.newFixedThreadPool(4)) {

        }*/
    }

    private static void executorServiceWithoutTry() {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<String>  submitFuture = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5L);
                return "Callable Done!";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        String s = null;
        try {
            s = submitFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println(s);

        executor.shutdown();
    }
}
