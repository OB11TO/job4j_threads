package ru.job4j.concurrent.blocking.exchanger.changer;

public final class ExchangerObject {

    private final long id;

    public ExchangerObject(final long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[id = " + this.id + "]";
    }
}