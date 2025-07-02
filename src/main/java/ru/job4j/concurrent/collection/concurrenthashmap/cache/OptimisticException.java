package ru.job4j.concurrent.collection.concurrenthashmap.cache;

public class OptimisticException extends RuntimeException {

    public OptimisticException(String message) {
        super(message);
    }
}