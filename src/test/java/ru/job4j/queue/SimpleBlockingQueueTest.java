package ru.job4j.queue;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleBlockingQueueTest {

    @Test
    public void whenNotEmptyQueue() {
        var simpleBlockingQueue = new SimpleBlockingQueue<Integer>();
        Thread producer1 = new Thread(
                () -> {
                    simpleBlockingQueue.offer(13);
                    simpleBlockingQueue.offer(11);
                    simpleBlockingQueue.offer(21);
                }
        );
        Thread consumer1 = new Thread(
                simpleBlockingQueue::poll
        );
        producer1.start();
        consumer1.start();
        try {
            producer1.join();
            consumer1.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertThat(simpleBlockingQueue.poll()).isEqualTo(11);
    }

    @Test
    public void whenQueueIsFullProducerWaits() {
        var simpleBlockingQueue = new SimpleBlockingQueue<Integer>();
        for (int i = 0; i < 10; i++) {
            simpleBlockingQueue.offer(i);
        }
        Thread producer = new Thread(() -> {
            simpleBlockingQueue.offer(99);
        });
        Thread consumer = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            simpleBlockingQueue.poll();
        });
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 9; i++) {
            simpleBlockingQueue.poll();
        }
        assertThat(simpleBlockingQueue.poll()).isEqualTo(99);
    }
}