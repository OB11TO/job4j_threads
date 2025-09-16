package ru.job4j.concurrent.future.city.ulit;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FutureUtil {

    public static <T> T get(final Future<T> future) {
        try {
            return future.get();
        } catch (final InterruptedException | ExecutionException cause) {
            throw new RuntimeException(cause);
        }
    }

    private FutureUtil() {

    }
}