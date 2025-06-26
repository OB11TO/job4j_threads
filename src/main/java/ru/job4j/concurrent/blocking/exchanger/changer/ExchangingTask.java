package ru.job4j.concurrent.blocking.exchanger.changer;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Exchanger;

import static java.lang.Thread.currentThread;

public abstract class ExchangingTask implements Runnable {
    private final Exchanger<Queue<ExchangerObject>> exchanger;
    private Queue<ExchangerObject> queue;

    protected ExchangingTask(Exchanger<Queue<ExchangerObject>> exchanger) {
        this.exchanger = exchanger;
        this.queue = new ArrayDeque<>();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            handle(queue);
            exchange();
        }
    }

    protected abstract void handle(Queue<ExchangerObject> queue);

    private void exchange() {
        try {
            this.queue = exchanger.exchange(queue);
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }
}
