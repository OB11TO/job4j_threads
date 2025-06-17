package ru.job4j.queue;

import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@ThreadSafe
public class SimpleBlockingQueue<T> {

    private static final int MAX_SIZE = 10;

    private final Object monitor = new Object();

    @GuardedBy("monitor")
    private final Queue<T> queue = new LinkedList<>();

    public void offer(T value) {
        synchronized (monitor) {
            while (queue.size() >= MAX_SIZE) {
                try {
                    log.info("Waiting for {}", value);
                    monitor.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            queue.offer(value);
            monitor.notifyAll();
        }
    }

    public T poll() {
        synchronized (monitor) {
            while (queue.isEmpty()) {
                try {
                    log.info("Waiting for poll");
                    monitor.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            monitor.notifyAll();
            return queue.poll();
        }
    }
}
