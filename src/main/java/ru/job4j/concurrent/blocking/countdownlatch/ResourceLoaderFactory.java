package ru.job4j.concurrent.blocking.countdownlatch;

import java.util.concurrent.CountDownLatch;

public final class ResourceLoaderFactory extends ResourceFactory {
    private long nextSecondDuration = 1;

    @Override
    protected ResourceLoader create(final long id, final CountDownLatch latch) {
        return new ResourceLoader(id, latch, this.nextSecondDuration++);
    }
}