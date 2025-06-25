package ru.job4j.concurrent.blocking.cyclibarrier;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public class LeafTask extends Task {

    private final long durationTimeWork;
    private final CyclicBarrier barrier;

    public LeafTask(long id, long durationTimeWork, CyclicBarrier barrier) {
        super(id);
        this.barrier = barrier;
        this.durationTimeWork = durationTimeWork;
    }

    @Override
    public void perform() {
        try {
            log.info("{} started perform {}", this.getId(), Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(3L);
            log.info("{} finished perform {}", this.getId(), Thread.currentThread().getName());
            barrier.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
