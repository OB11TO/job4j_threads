package ru.job4j.concurrent.collection.queue.broker;

import java.util.concurrent.BlockingQueue;

public class MessageBroker<T> {

    private final BlockingQueue<T> broker;

    public MessageBroker(final BlockingQueue<T> broker) {
        this.broker = broker;
    }

    public final T take() throws InterruptedException {
        return broker.take();
    }

    public final void put(final T value) throws InterruptedException {
        broker.put(value);
    }
}
