package ru.job4j.concurrent.collection.queue.task;

import ru.job4j.concurrent.collection.queue.broker.MessageBroker;

import java.util.concurrent.TimeUnit;

public abstract class MessageBrokerTask<T> implements Runnable {

    private final MessageBroker<T> messageBroker;
    private final long secondTimeout;

    protected MessageBrokerTask(MessageBroker<T> messageBroker, long secondTimeout) {
        this.messageBroker = messageBroker;
        this.secondTimeout = secondTimeout;
    }

    @Override
    public void run() {
        try {
            while (true) {
                executeOperation(messageBroker);
                TimeUnit.SECONDS.sleep(secondTimeout);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected abstract void executeOperation(MessageBroker<T> messageBroker) throws InterruptedException;
}
