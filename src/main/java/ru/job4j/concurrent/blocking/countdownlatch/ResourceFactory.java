package ru.job4j.concurrent.blocking.countdownlatch;

import java.util.concurrent.CountDownLatch;

public abstract class ResourceFactory {
    private long nextId;

    public final ResourceTask create(final CountDownLatch latch) {
        return this.create(this.nextId++, latch);
    }

    protected abstract ResourceTask create(final long id, final CountDownLatch latch);
}
