package ru.job4j.concurrent.blocking.countdownlatch;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class ResourceLoader extends ResourceTask {

    private final long secondDuration;

    public ResourceLoader(long id, CountDownLatch countDownLatch, long secondDuration) {
        super(id, countDownLatch);
        this.secondDuration = secondDuration;
    }

    @Override
    public void run(CountDownLatch countDownLatch) {
        try {
            log.info("{} is loading some resource ", this);
            SECONDS.sleep(this.secondDuration);
            log.info("Some resource was loaded by {} ", this);
        } catch (final InterruptedException interruptedException) {
            currentThread().interrupt();
        } finally {
            countDownLatch.countDown();
        }
    }
}
