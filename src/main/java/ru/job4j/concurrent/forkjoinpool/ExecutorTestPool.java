package ru.job4j.concurrent.forkjoinpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//public class ExecutorTestPool {
//
//    public static void main(String[] args) {
//        long start = System.currentTimeMillis();
//
//        // Создаем исполнитель, который для каждой задачи создает новый виртуальный поток
//        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
//            for (int i = 0; i < 1_000_000; i++) {
//                executorService.submit(() -> {
//                    // Здесь выполняется задача, например, короткий сон или I/O операция
//                    try {
//                        System.out.println("New task - " + Thread.currentThread().getName());
//                        Thread.sleep(1L);
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                    }
//                });
//            }
//        } // try-with-resources автоматически вызовет shutdown() и закроет сервис
//
//        long end = System.currentTimeMillis();
//        long duration = end - start;
//        System.out.println("Processed in: " + duration + " ms");
//    }
//}