package ru.job4j.concurrent.lock;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

@Slf4j
@Getter
public class EventGenerator {
    private int count;
    private final Lock lock = new ReentrantLock();

    public EventGenerator() {
        this.count = 0;
    }

    public int increment() {
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("Interrupted {}", Thread.currentThread().getName());
        }
        try {
            count += 2;
            return count;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        EventGenerator generator = new EventGenerator();
        Runnable runnable = () ->
                IntStream.range(0, 100).forEach(i -> System.out.println(generator.increment()));
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);
        thread1.start();
        thread2.start();
        thread3.start();
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(generator.getCount());
    }
}
