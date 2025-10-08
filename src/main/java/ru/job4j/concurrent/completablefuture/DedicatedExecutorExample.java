package ru.job4j.concurrent.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DedicatedExecutorExample {

    // Создаем пул потоков специально для блокирующих IO-операций
    private static final ExecutorService IO_EXECUTOR = Executors.newFixedThreadPool(10, r -> {
        Thread t = new Thread(r);
        t.setName("io-pool-thread-" + t.threadId());
        // Демон-потоки не мешают JVM завершиться
        t.setDaemon(true);
        return t;
    });

    public static String blockingDBOperation() {
        System.out.println("Executing blocking DB call in " + Thread.currentThread().getName());
        try {
            // Имитация долгого JDBC вызова
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Data from DB";
    }

    public static void main(String[] args) {
        System.out.println("Main thread: " + Thread.currentThread().getName());

        // --- НЕПРАВИЛЬНО: блокируем commonPool ---
        // CompletableFuture.supplyAsync(DedicatedExecutorExample::blockingDBOperation);
        // Так делать нельзя! Это приведет к starvation пула.

        // --- ПРАВИЛЬНО: используем свой Executor ---
        CompletableFuture<String> dbFuture = CompletableFuture.supplyAsync(
                DedicatedExecutorExample::blockingDBOperation,
                IO_EXECUTOR
        );

        dbFuture.thenAccept(result -> {
            System.out.println("Processing result in " + Thread.currentThread().getName());
            System.out.println("Result: " + result);
        });

        System.out.println("Main thread is not blocked.");

        // Даем время на выполнение, в реальном приложении это не нужно (это делаем, чтобы не ждать и не делать .join())
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        IO_EXECUTOR.shutdown(); // В реальных приложениях управляй жизненным циклом Executor'а (e.g., Spring Bean)
    }
}
