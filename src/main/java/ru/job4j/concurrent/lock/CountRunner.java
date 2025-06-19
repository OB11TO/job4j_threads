package ru.job4j.concurrent.lock;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

import static java.util.stream.IntStream.range;

@Slf4j
public class CountRunner {
    private final Lock lock = new ReentrantLock();
    @Getter
    private int count = 0;

    public void increment() {
        this.count++;
        printMessageWithCurrentThreadName("Current thread name: {} INCREMENT");
    }

    public void decrement() {
        this.count--;
        printMessageWithCurrentThreadName("Current thread name: {} DECREMENT");
    }

    public void lock() {
        this.lock.lock();
        printMessageWithCurrentThreadName("Current thread name: {} LOCK");
    }

    public void unlock() {
        this.lock.unlock();
        printMessageWithCurrentThreadName("Current thread name: {} UNLOCK");
    }

    public void printMessageWithCurrentThreadName(String message) {
        log.info(message, Thread.currentThread().getName());
    }

    public static Runnable createTaskDoingOperationOnCounter(CountRunner countRunner, IntConsumer action, int countOperation) {
        return () -> {
            countRunner.lock();
            try {
                range(0, countOperation).forEach(action);
            } finally {
                countRunner.unlock();
            }
        };
    }

    public static void main(String[] args) {
        CountRunner countRunner = new CountRunner();
        Runnable taskDoingOperationOnIncrementCounter = createTaskDoingOperationOnCounter(countRunner, i -> countRunner.increment(), 10);
        Runnable taskDoingOperationOnDecrementCounter = createTaskDoingOperationOnCounter(countRunner, i -> countRunner.decrement(), 10);

        Thread incrementThread1 = new Thread(taskDoingOperationOnIncrementCounter, "Thread 1 Increment");
        Thread incrementThread2 = new Thread(taskDoingOperationOnIncrementCounter, "Thread 2 Increment");

        Thread decrementThread1 = new Thread(taskDoingOperationOnDecrementCounter, "Thread 1 Decrement");
        Thread decrementThread2 = new Thread(taskDoingOperationOnDecrementCounter, "Thread 2 Decrement");

        startThread(incrementThread1, incrementThread2, decrementThread1, decrementThread2);
        joinThread(incrementThread1, incrementThread2, decrementThread1, decrementThread2);

        log.info("Counter - {}",countRunner.getCount());
    }


    private static void joinThread(Thread... threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void startThread(Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }
}
