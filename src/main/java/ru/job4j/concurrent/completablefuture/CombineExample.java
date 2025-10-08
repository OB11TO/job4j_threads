package ru.job4j.concurrent.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CombineExample {
    public static void main(String[] args) {
        System.out.println("Main thread: " + Thread.currentThread().getName());

        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> "ss")
                .thenApplyAsync(result -> "gg" + result)
                .thenCombine(CompletableFuture.supplyAsync(() -> "ff"), (result, other) -> result + " " + other);
        stringCompletableFuture.thenAccept(System.out::println);


        CompletableFuture<String> userNameFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Getting user name in " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return "John Doe";
        });

        CompletableFuture<Double> userBalanceFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Getting user balance in " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return 1000.50;
        });

        System.out.println("Futures are running in parallel...");

        CompletableFuture<String> combinedFuture = userNameFuture.thenCombine(
                userBalanceFuture,
                (name, balance) -> {
                    System.out.println("Combining results in " + Thread.currentThread().getName());
                    return "User: " + name + ", Balance: $" + balance;
                }
        );

        System.out.println("Waiting for combined result...");
        String result = combinedFuture.join();
        System.out.println("Final result: " + result);
    }
}