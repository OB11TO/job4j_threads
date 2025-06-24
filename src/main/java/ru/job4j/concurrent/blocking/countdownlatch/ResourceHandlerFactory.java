package ru.job4j.concurrent.blocking.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class ResourceHandlerFactory extends ResourceFactory {

    @Override
    protected ResourceTask create(long id, CountDownLatch latch) {
        return new ResourceHandler(id, latch);
    }
}
