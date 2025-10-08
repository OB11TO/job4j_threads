package ru.job4j.concurrent.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TimeoutExample {
    public static void main(String[] args) {
        // --- orTimeout: Завершить с исключением ---
        CompletableFuture<String> slowFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return "Finished slowly";
        });

        try {
            slowFuture.orTimeout(1, TimeUnit.SECONDS).join();
        } catch (Exception e) {
            System.out.println("orTimeout caught: " + e.getCause().getClass().getSimpleName()); // TimeoutException
        }

        // --- completeOnTimeout: Завершить с fallback значением ---
        CompletableFuture<String> slowFuture2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return "Finished slowly";
        });

        String result = slowFuture2.completeOnTimeout("Default Value", 1, TimeUnit.SECONDS).join();
        System.out.println("completeOnTimeout result: " + result);

        // --- Cancellation ---
        CompletableFuture<String> cancellableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Cancellable task starting...");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("This should not be printed if cancelled.");
            } catch (InterruptedException e) {
                System.out.println("Task was interrupted!");
                // Важно обрабатывать InterruptedException для корректной отмены
            }
            return "Not cancelled";
        });

        // Даем задаче немного поработать
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            System.out.println("Sleep interrupted!");
        }

        boolean cancelled = cancellableFuture.cancel(true); // true - пытаться прервать поток
        System.out.println("Was future cancelled? " + cancelled);

        assert (cancellableFuture.isCancelled());
        // join() на отмененном Future бросит CancellationException
    }
}
