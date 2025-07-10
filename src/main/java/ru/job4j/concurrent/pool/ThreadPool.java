package ru.job4j.concurrent.pool;

import ru.job4j.core.queue.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ThreadPool {
    private final List<Thread> threads;
    private final SimpleBlockingQueue<Runnable> tasks;

    public ThreadPool() {
        int nucleiNumber = Runtime.getRuntime().availableProcessors();
        this.tasks = new SimpleBlockingQueue<>();
        this.threads = createAndStartThreads(nucleiNumber);
    }

    private List<Thread> createAndStartThreads(int nucleiNumber) {
        return IntStream.range(0, nucleiNumber)
                .mapToObj(i -> new Thread(this::createTask, "Thread - " + i))
                .peek(Thread::start)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private void createTask() {
        while (!Thread.currentThread().isInterrupted()) {
            Runnable task;
            try {
                task = tasks.poll();
                task.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void work(Runnable task) {
        try {
            tasks.offer(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        threads.forEach(Thread::interrupt);
    }
}
