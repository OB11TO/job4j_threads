package ru.job4j.concurrent.blocking.countdownlatch;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class ResourceHandler extends ResourceTask {

    public ResourceHandler(long id, CountDownLatch countDownLatch) {
        super(id, countDownLatch);
    }

    @Override
    public void run(CountDownLatch countDownLatch) {
        try {
            countDownLatch.await();
            log.info("Resources were handled by {}", this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
