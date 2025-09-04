package ru.job4j.concurrent.blocking.cyclibarrier;

import java.util.concurrent.*;

public class ReportGenerationExample {
    public static void main(String[] args) throws InterruptedException {
        final int PARTIES = 3;
        ExecutorService exec = Executors.newFixedThreadPool(PARTIES);
        ConcurrentMap<String, String> parts = new ConcurrentHashMap<>();

        // Barrier action: агрегировать части отчёта
        Runnable aggregator = () -> {
            System.out.println(">>> Aggregator: собираю части отчёта...");
            String combined = parts.get("A") + "\n" + parts.get("B") + "\n" + parts.get("C");
            System.out.println(">>> Отчёт готов:\n" + combined);
            // Можно: сохранить в БД, создать PDF, отправить нотификацию и т.п.
            parts.clear(); // подготовка для следующей фазы, если нужно
        };

        CyclicBarrier barrier = new CyclicBarrier(PARTIES, aggregator);

        for (char c = 'A'; c <= 'C'; c++) {
            String partId = String.valueOf(c);
            exec.submit(() -> {
                try {
                    // 1) собрать/вычислить свою секцию
                    System.out.println("Task " + partId + " собирает данные...");
                    Thread.sleep(ThreadLocalRandom.current().nextInt(300, 1000));
                    parts.put(partId, "Data_of_" + partId);
                    System.out.println("Task " + partId + " готова, вызываю await()");
                    // 2) дождаться остальных
                    barrier.await(2, TimeUnit.SECONDS); // можно задать таймаут
                    // 3) дальше — возможна пост-агрегационная логика
                    System.out.println("Task " + partId + " продолжает после агрегации");
                } catch (TimeoutException te) {
                    System.err.println("Task " + partId + " timeout — кто-то слишком долго");
                    // Можно предпринять компенсационные действия или отменить процесс
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (BrokenBarrierException e) {
                    System.err.println("Barrier broken for " + partId);
                }
            });
        }

        exec.shutdown();
        exec.awaitTermination(5, TimeUnit.SECONDS);
    }
}
