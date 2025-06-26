package ru.job4j.concurrent.blocking.exchanger.changer;

import java.util.Queue;
import java.util.concurrent.Exchanger;
import java.util.stream.IntStream;

import static java.lang.System.out;

public final class ProducerTask extends ExchangingTask {

    private final ExchangerObjectFactory exchangerObjectFactory;
    private final int producedObjectCount;

    public ProducerTask(Exchanger<Queue<ExchangerObject>> exchanger, ExchangerObjectFactory exchangerObjectFactory, int producedObjectCount) {
        super(exchanger);
        this.exchangerObjectFactory = exchangerObjectFactory;
        this.producedObjectCount = producedObjectCount;
    }

    @Override
    protected void handle(Queue<ExchangerObject> queue) {
        IntStream.range(0, producedObjectCount)
                .mapToObj(i -> exchangerObjectFactory.createExchangerObject())
                .peek(object -> out.printf("%s is being produced\n", object))
                .forEach(queue::add);
    }
}
