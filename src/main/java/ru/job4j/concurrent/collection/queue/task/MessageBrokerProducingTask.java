package ru.job4j.concurrent.collection.queue.task;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.concurrent.collection.queue.broker.MessageBroker;

import java.util.function.Supplier;

@Slf4j
public class MessageBrokerProducingTask<T> extends MessageBrokerTask<T> {

   private final Supplier<T> factoryMessage;

    public MessageBrokerProducingTask(MessageBroker<T> messageBroker, long secondTimeout, Supplier<T> factoryMessage) {
        super(messageBroker, secondTimeout);
        this.factoryMessage = factoryMessage;
    }

    @Override
    protected void executeOperation(MessageBroker<T> messageBroker) throws InterruptedException {
        final T message = factoryMessage.get();
        messageBroker.put(message);
        log.info("Message Broker Producing send to {}", message);
    }
}
