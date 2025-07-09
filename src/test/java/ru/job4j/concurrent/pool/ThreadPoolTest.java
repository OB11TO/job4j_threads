package ru.job4j.concurrent.pool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

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
    void whenSubmitSingleTask_thenItExecutes() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        pool.work(latch::countDown);
        boolean completed = latch.await(2, TimeUnit.SECONDS);

        assertThat(completed)
                .as("Задача должна выполниться в пуле потоков")
                .isTrue();
    }

    @Test
    void whenSubmitMultipleTasks_thenAllExecute() throws InterruptedException {
        int tasksCount = 10;
        CountDownLatch latch = new CountDownLatch(tasksCount);
        AtomicInteger counter = new AtomicInteger();

        for (int i = 0; i < tasksCount; i++) {
            pool.work(() -> {
                counter.incrementAndGet();
                latch.countDown();
            });
        }
        boolean completed = latch.await(5, TimeUnit.SECONDS);

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
        pool.work(latch::countDown);

        boolean completed = latch.await(500, TimeUnit.MILLISECONDS);

        assertThat(completed)
                .as("После shutdown задачи не должны выполняться")
                .isFalse();
    }
}