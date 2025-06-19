package ru.job4j.core.queue;

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

    public void offer(T value) throws InterruptedException {
        synchronized (monitor) {
            while (queue.size() >= MAX_SIZE) {
                log.info("Waiting for {}", value);
                monitor.wait();
            }
            queue.offer(value);
            monitor.notifyAll();
        }
    }

    public T poll() throws InterruptedException {
        synchronized (monitor) {
            while (queue.isEmpty()) {
                log.info("Waiting for poll");
                monitor.wait();
            }
            monitor.notifyAll();
            return queue.poll();
        }
    }

    public boolean isEmpty() {
        synchronized (monitor) {
            return queue.isEmpty();
        }
    }
}
