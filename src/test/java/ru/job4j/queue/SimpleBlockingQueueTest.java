package ru.job4j.queue;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.Fail.fail;

class SimpleBlockingQueueTest {

    @Test
    void whenProduceThreeThenConsumeOneAndRemainingAreCorrect() {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        CountDownLatch latch = new CountDownLatch(2);

        Thread producer = new Thread(() -> {
            try {
                queue.offer(13);
                queue.offer(11);
                queue.offer(21);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Producer был прерван");
            } finally {
                latch.countDown();
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            try {
                Integer first = queue.poll();
                assertThat(first)
                        .isEqualTo(13);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Consumer был прерван");
            } finally {
                latch.countDown();
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        try {
            boolean completed = latch.await(1, TimeUnit.SECONDS);
            assertThat(completed)
                    .isTrue();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Тест прерван во время ожидания latch");
        }
        try {
            Integer next = queue.poll();
            assertThat(next)
                    .isEqualTo(11);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Проблема при poll() после синхронизации");
        }
    }

    @Test
    void whenQueueFullThenProducerBlocksUntilConsumerPolls() {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        for (int i = 0; i < 10; i++) {
            int value = i;
            assertThatCode(() -> queue.offer(value))
                    .as("offer(" + value + ") не должен кидать исключение")
                    .doesNotThrowAnyException();
        }
        CountDownLatch latch = new CountDownLatch(2);
        Thread producer = new Thread(() -> {
            try {
                queue.offer(99);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Producer был прерван");
            } finally {
                latch.countDown();
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
                Integer polled = queue.poll();
                assertThat(polled)
                        .isEqualTo(0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Consumer был прерван");
            } finally {
                latch.countDown();
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        try {
            boolean completed = latch.await(1, TimeUnit.SECONDS);
            assertThat(completed)
                    .isTrue();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Тест прерван во время ожидания latch");
        }

        for (int expected = 1; expected < 10; expected++) {
            try {
                Integer v = queue.poll();
                assertThat(v)
                        .as("Ожидали %d, получили %d", expected, v)
                        .isEqualTo(expected);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Ошибка при poll() элемента " + expected);
            }
        }
        try {
            Integer last = queue.poll();
            assertThat(last)
                    .isEqualTo(99);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Ошибка при poll() последнего элемента");
        }
    }
}
