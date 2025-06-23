package ru.job4j.concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class EventGeneratorNumber {

    private final AtomicInteger counter = new AtomicInteger();
    private static final int GENERATE_DELTA = 2;

    public void generate() {
        this.counter.addAndGet(GENERATE_DELTA);
    }

    public int getValue() {
        return this.counter.get();
    }

}
