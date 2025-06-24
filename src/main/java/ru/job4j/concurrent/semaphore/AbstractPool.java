package ru.job4j.concurrent.semaphore;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Slf4j
public abstract class AbstractPool<T> {

    private final List<PoolObject<T>> poolObjects;
    private final Semaphore semaphore;
    private final Lock lock = new ReentrantLock();

    public AbstractPool(final Supplier<T> supplier, final int size) {
        this.poolObjects = createPoolObjects(supplier, size);
        semaphore = new Semaphore(size);
    }

    private static <T> List<PoolObject<T>> createPoolObjects(Supplier<T> supplier, int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> supplier.get())
                .map(obj -> new PoolObject<>(obj, false))
                .toList();
    }

    public final T acquire() {
        semaphore.acquireUninterruptibly();
        lock.lock();
        try {
            return poolObjects.stream()
                    .filter(p -> !p.isIssued())
                    .findFirst()
                    .map(poolObj -> {
                        poolObj.setIssued(true);
                        return poolObj.getValue();
                    })
                    .orElseThrow(IllegalStateException::new);
        } finally {
            lock.unlock();
        }
    }

    public final void release(T value) {
        lock.lock();
        try {
            if (releaseObject(value)) {
                semaphore.release();
            }
        } finally {
            lock.unlock();
        }
    }

    private boolean releaseObject(T value) {
        lock.lock();
        try {
            return this.poolObjects.stream()
                    .filter(PoolObject::isIssued)
                    .filter(poolObject -> Objects.equals(poolObject.getValue(), value))
                    .findFirst()
                    .map(this::cleanPoolObject)
                    .isPresent();
        } finally {
            lock.unlock();
        }
    }

    private PoolObject<T> cleanPoolObject(PoolObject<T> poolObject) {
        lock.lock();
        try {
            poolObject.setIssued(false);
            this.cleanObject(poolObject.getValue());
            return poolObject;
        } finally {
            lock.unlock();
        }
    }

    protected abstract void cleanObject(T value);
}
