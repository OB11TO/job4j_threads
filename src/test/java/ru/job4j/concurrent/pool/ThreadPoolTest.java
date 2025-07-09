package ru.job4j.concurrent.pool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class ThreadPoolTest {

    private ThreadPool pool;

    @BeforeEach
    void setUp() {
        pool = new ThreadPool();
    }

    @AfterEach
    void tearDown() {
        pool.shutdown();
    }

    @Test
    void whenSubmitSingleTaskThenItExecutes() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        pool.work(latch::countDown);
        boolean completed = latch.await(2L, TimeUnit.SECONDS);

        assertThat(completed)
                .as("Задача должна выполниться в пуле потоков")
                .isTrue();
    }

    @Test
    void whenSubmitMultipleTasksThenAllExecute() throws InterruptedException {
        int tasksCount = 10;
        CountDownLatch latch = new CountDownLatch(tasksCount);
        AtomicInteger counter = new AtomicInteger();

        for (int i = 0; i < tasksCount; i++) {
            pool.work(() -> {
                counter.incrementAndGet();
                latch.countDown();
            });
        }
        boolean completed = latch.await(5L, TimeUnit.SECONDS);

        assertThat(completed)
                .as("Все задачи должны выполниться в пуле потоков")
                .isTrue();
        assertThat(counter.get())
                .as("Счётчик должен быть равен количеству задач")
                .isEqualTo(tasksCount);
    }

    @Test
    void afterShutdownNoFurtherTasksExecute() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        pool.shutdown();
        TimeUnit.SECONDS.sleep(1L);
        pool.work(latch::countDown);
        boolean completed = latch.await(1L, TimeUnit.SECONDS);

        assertThat(completed)
                .as("После shutdown задачи не должны выполняться")
                .isFalse();
    }
}