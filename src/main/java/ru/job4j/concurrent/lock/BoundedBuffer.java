package ru.job4j.concurrent.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer<T> {
    private final Lock lock;
    private final Condition isEmpty;
    private final Condition isFull;
    private final T[] buffer;
    private int size;

    @SuppressWarnings("unchecked")
    public BoundedBuffer(int capacity) {
        this.buffer = (T[]) new Object[capacity];
        this.lock = new ReentrantLock();
        this.isEmpty = this.lock.newCondition();
        this.isFull = this.lock.newCondition();
    }

    public boolean isEmpty() {
        lock.lock();
        try {
            return size == 0;
        } finally {
            lock.unlock();
        }
    }

    public boolean isFull() {
        lock.lock();
        try {
            return size == buffer.length;
        } finally {
            lock.unlock();
        }
    }

    public void put(T value) {
        lock.lock();
        try {
            while (isFull()) {
                isFull.await();
            }
            buffer[size++] = value;
            isEmpty.signal();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public T poll() {
        lock.lock();
        try {
            while (isEmpty()) {
                isEmpty.await();
            }
            T element = buffer[size - 1];
            buffer[size - 1] = null;
            size--;
            isFull.signal();
            return element;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException();
        } finally {
            lock.unlock();
        }
    }
}
