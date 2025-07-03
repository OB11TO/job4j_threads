package ru.job4j.concurrent.collection.queue.broker;

import java.util.concurrent.SynchronousQueue;

public class SynchronousMessageBroker<T> extends MessageBroker<T> {

    public SynchronousMessageBroker() {
        super(new SynchronousQueue<>());
    }
}
