package ru.job4j.concurrent.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class ExceptionHandlingExample {
    public static void main(String[] args) throws InterruptedException {
        // --- exceptionally: Fallback ---
        CompletableFuture<String> futureWithFallback = CompletableFuture.supplyAsync(() -> {
            if (ThreadLocalRandom.current().nextBoolean()) {
                throw new RuntimeException("Oops, something went wrong!");
            }
            return "Success!";
        }).exceptionally(ex -> {
            System.out.println("Caught exception: " + ex.getMessage());
            return "Fallback Value"; // Возвращаем значение по умолчанию
        });
        System.out.println("Result with exceptionally: " + futureWithFallback.join());

        // --- handle: Обработка и успеха, и ошибки ---
        CompletableFuture<String> futureWithHandle = CompletableFuture.supplyAsync(() -> {
            if (ThreadLocalRandom.current().nextBoolean()) {
                throw new RuntimeException("Another issue!");
            }
            return "Success again!";
        }).handle((result, ex) -> {
            if (ex != null) {
                System.out.println("Handled exception: " + ex.getMessage());
                return "Handled Fallback";
            }
            return "Handled " + result;
        });
        System.out.println("Result with handle: " + futureWithHandle.join());

        // --- whenComplete: Для логирования (не меняет результат) ---
        CompletableFuture<String> futureWithLogging = CompletableFuture.supplyAsync(() -> "Final Success")
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        System.err.println("Logging exception: " + ex.getMessage());
                    } else {
                        System.out.println("Logging successful result: " + result);
                    }
                });
        futureWithLogging.join();
    }
}
