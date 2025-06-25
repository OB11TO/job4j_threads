package ru.job4j.concurrent.blocking.semaphore;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Runner {
    public static void main(String[] args) {
        int sizePollConnection = 3;
        ConnectionPool connectionPool = new ConnectionPool(sizePollConnection);
        int amountConnectionTask = 10;
        var connectionPoolTask = generateConnectionPoolTask(connectionPool, amountConnectionTask);
        Thread[] threads = generateThreadsTask(connectionPoolTask);

        startThreadsTask(threads);
        waitThreadsTask(threads);

    }

    private static void waitThreadsTask(Thread[] threads) {
        Arrays.stream(threads).forEach(Runner::waitThreadTask);
    }

    private static void waitThreadTask(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void startThreadsTask(Thread[] threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }

    private static Thread[] generateThreadsTask(List<ConnectionPoolTask> connectionPoolTask) {
        return connectionPoolTask.stream()
                .map(Thread::new)
                .toArray(Thread[]::new);
    }

    private static List<ConnectionPoolTask> generateConnectionPoolTask(ConnectionPool connectionPool, int amountConnectionTask) {
        return IntStream.range(0, amountConnectionTask)
                .mapToObj(i -> new ConnectionPoolTask(connectionPool))
                .toList();
    }
}
