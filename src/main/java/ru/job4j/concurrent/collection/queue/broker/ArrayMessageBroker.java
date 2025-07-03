package ru.job4j.concurrent.collection.queue.broker;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayMessageBroker<T> extends MessageBroker<T> {

    public ArrayMessageBroker(int sizeCapacity) {
        super(new ArrayBlockingQueue<>(sizeCapacity));
    }
}
