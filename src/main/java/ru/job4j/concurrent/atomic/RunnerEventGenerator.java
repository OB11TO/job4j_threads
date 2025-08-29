package ru.job4j.concurrent.atomic;

import java.util.Arrays;
import java.util.stream.IntStream;

public class RunnerEventGenerator {

    public static void main(String[] args) {
        final EventGeneratorNumber generator = new EventGeneratorNumber();

        int amountTask = 10000;
        Runnable generateTask = () -> IntStream.range(0, amountTask).forEach(i -> generator.generate());
        int countThreads = 4;
        Thread[] threads = generateThreads(generateTask, countThreads);

        startThreads(threads);
        waitUtilityFinish(threads);

        int expectedAmount = 2 * countThreads * amountTask;
        int resultAmount = generator.getValue();
        if (resultAmount != expectedAmount) {
            throw new RuntimeException("GG");
        }

    }
    private static void waitUtilityFinish(Thread[] threads) {
        Arrays.stream(threads).forEach(RunnerEventGenerator::waitUtilityFinish);
    }

    private static void waitUtilityFinish(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startThreads(Thread[] threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }

    private static Thread[] generateThreads(Runnable task, int countThreads) {
        return IntStream.range(0, countThreads)
                .mapToObj(i -> new Thread(task))
                .toArray(Thread[]::new);
    }
}
