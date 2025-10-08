package ru.job4j.concurrent.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class HelloWorldExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread: " + Thread.currentThread().getName());

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Async task thread: " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Hello, Async World!";
        });

        /* thenAccept выполнится, когда future завершится */
        future.thenAccept(result -> {
            System.out.println("Callback thread: " + Thread.currentThread().getName());
            System.out.println("Result: " + result);
        });

        System.out.println("Main thread continues to run...");
        TimeUnit.SECONDS.sleep(2); /* Даем время асинхронной задаче завершиться */
    }
}
