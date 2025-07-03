package ru.job4j.concurrent.collection.queue.task;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.concurrent.collection.queue.broker.MessageBroker;

@Slf4j
public class MessageBrokerConsumingTask<T> extends MessageBrokerTask<T> {

    public MessageBrokerConsumingTask(MessageBroker<T> messageBroker, long secondTimeout) {
        super(messageBroker, secondTimeout);
    }

    @Override
    protected void executeOperation(MessageBroker<T> messageBroker) throws InterruptedException {
        T message = messageBroker.take();
        log.info("Message consume: {}", message);
    }
}
