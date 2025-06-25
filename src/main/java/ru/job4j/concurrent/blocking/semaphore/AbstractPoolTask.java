package ru.job4j.concurrent.blocking.semaphore;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class AbstractPoolTask<T> implements Runnable {

    private final AbstractPool<T> abstractPool;

    protected AbstractPoolTask(AbstractPool<T> abstractPool) {
        this.abstractPool = abstractPool;
    }

    @Override
    public void run() {
        T object = abstractPool.acquire();
        try {
            log.info("{} was acquired, object is {}", Thread.currentThread().getName(), object);
            this.handle(object);
        } finally {
            log.info("{} start release, object is {}", Thread.currentThread().getName(), object);
            abstractPool.release(object);
        }
    }

    public abstract void handle(T object);
}
