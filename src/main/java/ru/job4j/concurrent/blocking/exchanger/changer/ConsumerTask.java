package ru.job4j.concurrent.blocking.exchanger.changer;

import java.util.Queue;
import java.util.concurrent.Exchanger;

import static java.lang.System.out;

public final class ConsumerTask extends ExchangingTask {

    public ConsumerTask(Exchanger<Queue<ExchangerObject>> exchanger) {
        super(exchanger);
    }

    @Override
    protected void handle(Queue<ExchangerObject> queue) {
        while (!queue.isEmpty()) {
            ExchangerObject exchangerObject = queue.poll();
            out.printf("%s was consumed\n", exchangerObject);
        }
        out.println("---------------------------------------------");
    }
}

