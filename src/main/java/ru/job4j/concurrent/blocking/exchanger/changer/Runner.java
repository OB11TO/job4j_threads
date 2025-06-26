package ru.job4j.concurrent.blocking.exchanger.changer;

import java.util.Queue;
import java.util.concurrent.Exchanger;

public class Runner {
    public static void main(String[] args) {
        final Exchanger<Queue<ExchangerObject>> exchanger = new Exchanger<>();
        final int producedObjectCount = 3;
        final ExchangerObjectFactory factory = new ExchangerObjectFactory();
        final ProducerTask producerTask = new ProducerTask(exchanger, factory, producedObjectCount);
        final ConsumerTask consumerTask = new ConsumerTask(exchanger);

        new Thread(producerTask).start();
        new Thread(consumerTask).start();
    }
}
