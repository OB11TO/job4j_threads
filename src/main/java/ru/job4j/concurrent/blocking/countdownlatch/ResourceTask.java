package ru.job4j.concurrent.blocking.countdownlatch;

import java.util.concurrent.CountDownLatch;

public abstract class ResourceTask implements Runnable {
    private final long id;
    private final CountDownLatch countDownLatch;

    public ResourceTask(final long id, final CountDownLatch countDownLatch) {
        this.id = id;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        this.run(this.countDownLatch);
    }

    public abstract void run(CountDownLatch countDownLatch);

    @Override
    public final String toString() {
        return this.getClass().getName() + "[id = " + this.id + "]";
    }
}
