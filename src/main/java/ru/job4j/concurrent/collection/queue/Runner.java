package ru.job4j.concurrent.collection.queue;

import ru.job4j.concurrent.collection.queue.broker.ArrayMessageBroker;
import ru.job4j.concurrent.collection.queue.broker.MessageBroker;
import ru.job4j.concurrent.collection.queue.broker.SynchronousMessageBroker;
import ru.job4j.concurrent.collection.queue.task.MessageBrokerConsumingTask;
import ru.job4j.concurrent.collection.queue.task.MessageBrokerProducingTask;

import java.util.concurrent.ThreadLocalRandom;

public class Runner {

    public static void main(String[] args) {
        MessageBroker<Integer> messageBroker = new SynchronousMessageBroker<>();

        startProducing(messageBroker, 3L);
        startProducing(messageBroker, 5L);
        startProducing(messageBroker, 1L);

        startConsuming(messageBroker, 1L);
        startConsuming(messageBroker, 5L);
        startConsuming(messageBroker, 3L);
    }

    private static void startProducing(MessageBroker<Integer> broker, long secondTimeout) {
        new Thread(new MessageBrokerProducingTask<>(broker, secondTimeout, Runner::generateInt)).start();
    }

    private static void startConsuming(MessageBroker<Integer> broker, long secondTimeout) {
        new Thread(new MessageBrokerConsumingTask<>(broker, secondTimeout)).start();
    }

    private static int generateInt() {
        return ThreadLocalRandom.current().nextInt(0, 10);
    }
}
