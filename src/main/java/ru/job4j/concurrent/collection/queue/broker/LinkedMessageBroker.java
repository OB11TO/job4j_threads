package ru.job4j.concurrent.collection.queue.broker;

import java.util.concurrent.LinkedBlockingQueue;

public class LinkedMessageBroker<T> extends MessageBroker<T> {

    public LinkedMessageBroker(int sizeCapacity) {
        super(new LinkedBlockingQueue<>(sizeCapacity));
    }
}
