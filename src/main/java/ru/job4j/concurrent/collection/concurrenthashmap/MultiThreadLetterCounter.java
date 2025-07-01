package ru.job4j.concurrent.collection.concurrenthashmap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class MultiThreadLetterCounter extends LetterCounter {

    public MultiThreadLetterCounter(int subtaskCount) {
        super(subtaskCount);
    }

    @Override
    protected void executeRunner(Stream<Subtask> subtasks) {
        List<Thread> threads = run(subtasks);
        waitingThreads(threads);
    }

    private void waitingThreads(List<Thread> threads) {
        threads.forEach(this::waitingThread);
    }

    private void waitingThread(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private List<Thread> run(Stream<Subtask> subtasks) {
        return subtasks.map(this::run).toList();
    }

    private Thread run(Subtask subtasks) {
        Thread thread = new Thread(subtasks::execute);
        thread.start();
        return thread;
    }

    @Override
    protected Map<Character, Integer> createAccumulator() {
        return new ConcurrentHashMap<>();
    }
}

