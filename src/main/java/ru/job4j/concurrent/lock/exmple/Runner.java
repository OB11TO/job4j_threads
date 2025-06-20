package ru.job4j.concurrent.lock.exmple;

import lombok.Getter;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Runner {

    public static void main(String[] args) throws InterruptedException {
        testCounter(CounterGuardedByReentrantLock::new); /* 216245925 vs 25824217 */
        testCounter(CounterGuardedByReentrantReadWriterLock::new);
    }

    private static void testCounter(final Supplier<? extends AbstractCounter> counterFactory) throws InterruptedException {
        final AbstractCounter counter = counterFactory.get();
        final int amountOfThreadsGettingValue = 50;
        final ReadingValueTask[] readingValueTasks = generateReadingTasks(counter, amountOfThreadsGettingValue);
        final Thread[] threadsOfReadingTask = generateThreadsOfReadingTask(readingValueTasks);

        final int amountOfThreadsIncrementThreads = 2;
        final Runnable incrementCounterTask = generateIncrementCounterTask(counter);
        final Thread[] threadsOfWritingIncrementTask = generateThreadsOfWritingIncrementTask(incrementCounterTask, amountOfThreadsIncrementThreads);

        startThreads(threadsOfReadingTask);
        startThreads(threadsOfWritingIncrementTask);

        TimeUnit.SECONDS.sleep(5L);

        interruptThreads(threadsOfReadingTask);
        interruptThreads(threadsOfWritingIncrementTask);

        waitUntilFinish(threadsOfReadingTask);

        final long totalAmountOfThreads = findTotalAmountOfReads(readingValueTasks);
        System.out.println(totalAmountOfThreads);

    }

    private static long findTotalAmountOfReads(final ReadingValueTask[] tasks) {
        return Arrays.stream(tasks)
                .mapToLong(ReadingValueTask::getAmountOfReads)
                .sum();
    }

    private static void interruptThreads(final Thread[] threads) {
        forEachThreads(threads, Thread::interrupt);
    }

    private static void waitUntilFinish(final Thread[] threads) {
        forEachThreads(threads, Runner::waitUntilFinish);
    }

    private static void waitUntilFinish(final Thread thread) {
        try {
            thread.join();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void startThreads(Thread[] threads) {
        forEachThreads(threads, Thread::start);
    }

    private static void forEachThreads(Thread[] threads, Consumer<Thread> action) {
        Arrays.stream(threads).forEach(action);
    }

    private static Thread[] generateThreadsOfWritingIncrementTask(final Runnable incrementCounterTask, int amountOfThreadsIncrementThreads) {
        return IntStream.range(0, amountOfThreadsIncrementThreads)
                .mapToObj(i -> new Thread(incrementCounterTask))
                .toArray(Thread[]::new);
    }

    private static Runnable generateIncrementCounterTask(final AbstractCounter counter) {
        return () -> {
            while (!Thread.currentThread().isInterrupted()) {
                incrementCounter(counter);
            }
        };
    }

    private static void incrementCounter(final AbstractCounter counter) {
        try {
            counter.increment();
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private static ReadingValueTask[] generateReadingTasks(final AbstractCounter counter, final int amountOfTaskReading) {
        return IntStream.range(0, amountOfTaskReading)
                .mapToObj(i -> new ReadingValueTask(counter))
                .toArray(ReadingValueTask[]::new);
    }

    private static Thread[] generateThreadsOfReadingTask(final Runnable[] readingValueTasks) {
        return Arrays.stream(readingValueTasks)
                .map(Thread::new)
                .toArray(Thread[]::new);
    }

    private final static class ReadingValueTask implements Runnable {

        private final AbstractCounter abstractCounter;

        @Getter
        private long amountOfReads;

        private ReadingValueTask(AbstractCounter abstractCounter) {
            this.abstractCounter = abstractCounter;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                abstractCounter.getVal();
                amountOfReads++;
            }
        }
    }
}
