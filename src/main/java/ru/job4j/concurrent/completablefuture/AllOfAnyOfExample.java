package ru.job4j.concurrent.completablefuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AllOfAnyOfExample {

    static CompletableFuture<String> createFuture(String name, int delay) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Task " + name + " started.");
                TimeUnit.SECONDS.sleep(delay);
                System.out.println("Task " + name + " finished.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Result of " + name;
        });
    }

    public static void main(String[] args) {
        // --- allOf Example ---
        System.out.println("--- allOf: Waiting for all futures to complete ---");
        List<CompletableFuture<String>> futures = List.of(
                createFuture("A", 1),
                createFuture("B", 3),
                createFuture("C", 2)
        );

        // CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])) возвращает CompletableFuture<Void>
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // Чтобы получить результаты, нужно дождаться allOf, а потом собрать их
        CompletableFuture<List<String>> allResultsFuture = allFutures.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join) // join() здесь безопасен, так как все futures уже завершены
                        .collect(Collectors.toList())
        );

        List<String> results = allResultsFuture.join();
        System.out.println("All results: " + results);

        // --- anyOf Example ---
        System.out.println("\n--- anyOf: Waiting for the first future to complete ---");
        CompletableFuture<String> future1 = createFuture("Fast", 1);
        CompletableFuture<String> future2 = createFuture("Slow", 3);

        CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(future1, future2);

        Object firstResult = anyFuture.join();
        System.out.println("First result: " + firstResult); // Будет "Result of Fast"

        // Даем время второму future завершиться, чтобы не прерывать вывод
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
