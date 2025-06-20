package ru.job4j.concurrent.lock.exmple;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterGuardedByReentrantLock extends AbstractCounter {

    private final Lock lock = new ReentrantLock();

    @Override
    protected Lock getReaderLock() {
        return this.lock;
    }

    @Override
    protected Lock getWriterLock() {
        return this.lock;
    }
}
