package ru.job4j.concurrent.atomic;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class CASCount {
    private final AtomicInteger count = new AtomicInteger();

    public void increment() {
        this.count.incrementAndGet();
    }

    public int get() {
        return this.count.get();
    }
}