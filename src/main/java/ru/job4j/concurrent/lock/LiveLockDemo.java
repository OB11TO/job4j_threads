package ru.job4j.concurrent.lock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class LiveLockDemo {
    private static final ReentrantLock lockA = new ReentrantLock();
    private static final ReentrantLock lockB = new ReentrantLock();

    public static void main(String[] args) {
        Runnable worker1 = () -> work(lockA, lockB);
        Runnable worker2 = () -> work(lockB, lockA);

        new Thread(worker1, "W1").start();
        new Thread(worker2, "W2").start();
    }

    @SneakyThrows
    private static void work(ReentrantLock first, ReentrantLock second) {
        String name = Thread.currentThread().getName();
        log.info("Name: {}!", name);
        first.lock();
        try {
            log.info("Name: {} LOCK!", name);
            TimeUnit.MILLISECONDS.sleep(500L);
            while (!second.tryLock()) {
                log.info("Name: {} RETRY!", name);
                first.unlock();
                TimeUnit.MILLISECONDS.sleep(500L);
                first.lock();
                TimeUnit.MILLISECONDS.sleep(500L);
            }
        } finally {
            first.unlock();
            log.info("Name: {} UNLOCK", name);
        }
    }
}



