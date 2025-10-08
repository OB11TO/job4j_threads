package ru.job4j.concurrent.completablefuture;

import java.util.concurrent.CompletableFuture;

public class ApplyVsComposeExample {

    // Имитация асинхронного вызова, который получает User ID
    static CompletableFuture<String> getUserIdAsync() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Getting User ID in " + Thread.currentThread().getName());
            return "user-123";
        });
    }

    // Имитация асинхронного вызова, который получает детали пользователя по ID
    static CompletableFuture<String> getUserDetailsAsync(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Getting User Details for " + userId + " in " + Thread.currentThread().getName());
            return "Details for " + userId;
        });
    }

    public static void main(String[] args) {
        System.out.println("--- Using thenApply (the wrong way) ---");
        // Проблема: мы получаем CompletableFuture<CompletableFuture<String>>
        CompletableFuture<CompletableFuture<String>> nestedFuture = getUserIdAsync()
                //.thenApply(s -> CompletableFuture.supplyAsync(() -> s + "GG"))
                .thenApply(ApplyVsComposeExample::getUserDetailsAsync);

        // Чтобы получить результат, нужно дважды вызывать join() - неудобно!
        String result1 = nestedFuture.join().join();
        System.out.println("Nested result: " + result1);

        System.out.println("\n--- Using thenCompose (the right way) ---");
        // Решение: thenCompose "распаковывает" вложенный Future
        CompletableFuture<String> flatFuture = getUserIdAsync()
                .thenCompose(ApplyVsComposeExample::getUserDetailsAsync);

        String result2 = flatFuture.join();
        System.out.println("Flat result: " + result2);
    }
}