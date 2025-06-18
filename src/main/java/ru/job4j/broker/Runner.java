package ru.job4j.broker;

import static java.util.Arrays.stream;

public class Runner {
    public static void main(String[] args) {
        final int brokerMaxMSGs = 15;
        final MessageBroker messageBroker = new MessageBroker(brokerMaxMSGs);

        final MessageFactory messageFactory = new MessageFactory();

        final Thread producingThread1 = new Thread(new MessageProducing(messageBroker, messageFactory,
                brokerMaxMSGs, "Producer 1"));
        final Thread producingThread2 = new Thread(new MessageProducing(messageBroker, messageFactory,
                10, "Producer 2"));
        final Thread producingThread3 = new Thread(new MessageProducing(messageBroker, messageFactory,
                5, "Producer 3"));

        final Thread consumingThread1 = new Thread(new MessageConsuming(messageBroker,
                0, "Consumer 1"));
        final Thread consumingThread2 = new Thread(new MessageConsuming(messageBroker,
                6, "Consumer 2"));
        final Thread consumingThread3 = new Thread(new MessageConsuming(messageBroker,
                11, "Consumer 3"));

        startThreads(producingThread1, producingThread2, producingThread3,
                consumingThread1, consumingThread2, consumingThread3);
    }

    private static void startThreads(final Thread... threads) {
        stream(threads).forEach(Thread::start);
    }
}