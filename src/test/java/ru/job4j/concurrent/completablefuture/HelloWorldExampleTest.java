package ru.job4j.concurrent.completablefuture;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloWorldExampleTest {
    @Test
    void testHelloWorldAsync() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");

        String result = future.join(); /* .join() блокирует тестовый поток для получения результата */

        assertEquals("Hello", result);
    }

    @Test
    void testWithCompletedFuture() {
        // Используем already-completed future для тестирования логики колбэков
        CompletableFuture<String> completedFuture = CompletableFuture.completedFuture("Test");

        completedFuture.thenAccept(result -> {
            assertEquals("Test", result);
        }).join();
    }
}