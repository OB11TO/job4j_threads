package ru.job4j.concurrent.lock.exmple;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CounterGuardedByReentrantReadWriterLock extends AbstractCounter {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Override
    protected Lock getReaderLock() {
        return this.readWriteLock.readLock();
    }

    @Override
    protected Lock getWriterLock() {
        return this.readWriteLock.writeLock();
    }
}
