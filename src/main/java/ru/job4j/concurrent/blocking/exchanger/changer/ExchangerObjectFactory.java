package ru.job4j.concurrent.blocking.exchanger.changer;

import java.util.concurrent.TimeUnit;

public final class ExchangerObjectFactory {
    private long nextId;

    public ExchangerObject createExchangerObject() {
        try {
            TimeUnit.SECONDS.sleep(3);
            return new ExchangerObject(this.nextId++);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
