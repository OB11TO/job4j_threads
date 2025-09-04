package ru.job4j.concurrent.blocking.cyclibarrier;

import java.util.concurrent.*;

public class SimpleCyclicBarrierExample {
    public static void main(String[] args) {
        final int PARTIES = 3;
        CyclicBarrier barrier = new CyclicBarrier(PARTIES, () -> System.out.println("Barrier tripped — все готовы! 🎉"));

        ExecutorService exec = Executors.newFixedThreadPool(PARTIES);
        for (int i = 1; i <= PARTIES; i++) {
            final int id = i;
            exec.submit(() -> {
                try {
                    System.out.println("Worker " + id + " готовится...");
                    Thread.sleep(500L * id); // имитация подготовки
                    System.out.println("Worker " + id + " вызвал await()");
                    barrier.await(); // ждём остальных
                    System.out.println("Worker " + id + " продолжает работу 🚀");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (BrokenBarrierException e) {
                    System.out.println("Barrier сломан для worker " + id);
                }
            });
        }
        exec.shutdown();
    }
}
