package ru.job4j.concurrent.future.city.ulit;

import java.util.Iterator;
import java.util.List;

import static java.util.stream.StreamSupport.stream;

public final class IteratorUtil {

    public static <T> List<T> asList(final Iterator<T> iterator) {
        final Iterable<T> iterable = () -> iterator;
        return stream(iterable.spliterator(), false).toList();
    }

    private IteratorUtil() {

    }
}