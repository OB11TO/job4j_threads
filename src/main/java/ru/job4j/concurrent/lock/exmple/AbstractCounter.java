package ru.job4j.concurrent.lock.exmple;

import java.util.concurrent.locks.Lock;

public abstract class AbstractCounter {

    private int val;

    public AbstractCounter() {
        this.val = 0;
    }

    public final int getVal() {
        Lock lock = this.getReaderLock();
        lock.lock();
        try {
            return this.val;
        } finally {
            lock.unlock();
        }

    }

    public final void increment() {
        Lock lock = this.getWriterLock();
        lock.lock();
        try {
            val++;
        } finally {
            lock.unlock();
        }
    }

    protected abstract Lock getReaderLock();

    protected abstract Lock getWriterLock();
}
