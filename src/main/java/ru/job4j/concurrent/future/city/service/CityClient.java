package ru.job4j.concurrent.future.city.service;

import ru.job4j.concurrent.future.city.model.City;
import ru.job4j.concurrent.future.city.model.Coordinate;

import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.concurrent.TimeUnit.SECONDS;


public final class CityClient {
    private static final long SECOND_TIMEOUT = 1;
    private static final long MIN_ID = 0;
    private static final long MAX_ID = 10;

    public City request(@SuppressWarnings("unused") final Coordinate coordinate) {
        try {
            SECONDS.sleep(SECOND_TIMEOUT);
            final long id = current().nextLong(MIN_ID, MAX_ID + 1);
            return new City(id);
        } catch (final InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }
}