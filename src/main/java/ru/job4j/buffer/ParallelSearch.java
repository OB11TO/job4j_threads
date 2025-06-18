package ru.job4j.buffer;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.queue.SimpleBlockingQueue;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
public class ParallelSearch {

    public static void main(String[] args) {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<Integer>();
        final Thread consumer = getThreadConsumer(queue);
        new Thread(
                () -> {
                    for (int index = 0; index != 3; index++) {
                        try {
                            queue.offer(index);
                            MILLISECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage(), e);
                            Thread.currentThread().interrupt();
                        }
                    }
                    consumer.interrupt();
                }

        ).start();
    }

    private static Thread getThreadConsumer(SimpleBlockingQueue<Integer> queue) {
        final Thread consumer = new Thread(
                () -> {
                    try {
                        while (!Thread.currentThread().isInterrupted()) {
                            MILLISECONDS.sleep(400);
                            log.info("Consumer get {} ", queue.poll());
                        }
                    } catch (InterruptedException e) {
                        log.info("Consumer interrupted");
                        Thread.currentThread().interrupt();
                    }
                }
        );
        consumer.start();
        return consumer;
    }
}