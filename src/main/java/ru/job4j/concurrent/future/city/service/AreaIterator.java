package ru.job4j.concurrent.future.city.service;

import ru.job4j.concurrent.future.city.model.Area;
import ru.job4j.concurrent.future.city.model.Coordinate;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AreaIterator implements Iterator<Coordinate> {

    private static final double STEP = 0.5;

    private final Area area;

    private Coordinate cursor;

    public AreaIterator(Area area) {
        this.area = area;
        setCursorBeforeFirst(area);
    }

    private void setCursorBeforeFirst(final Area area) {
        final double latitude = area.leftBottom().latitude() - STEP;
        final double longitude = area.leftBottom().longitude();
        cursor = new Coordinate(latitude, longitude);
    }

    @Override
    public boolean hasNext() {
        return hasNextLatitude() || hasNextLongitude();
    }

    private boolean hasNextLatitude() {
        return Double.compare(cursor.latitude(), area.rightUpper().latitude()) < 0;
    }

    private boolean hasNextLongitude() {
        return Double.compare(cursor.longitude(), area.rightUpper().longitude()) < 0;
    }

    @Override
    public Coordinate next() {
        if (hasNextLatitude()) {
            return nextLatitude();
        } else if (hasNextLongitude()) {
            return nextLongitude();
        }
        throw new NoSuchElementException();
    }

    private Coordinate nextLatitude() {
        final double next = Math.min(cursor.latitude() + STEP, area.rightUpper().latitude());
        cursor = new Coordinate(next, cursor.longitude());
        return cursor;
    }

    private Coordinate nextLongitude() {
        final double next = Math.min(cursor.longitude() + STEP, area.rightUpper().longitude());
        cursor = new Coordinate(area.leftBottom().latitude(), next);
        return cursor;
    }
}
