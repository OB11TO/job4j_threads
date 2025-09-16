package ru.job4j.concurrent.future.city.model;

import java.util.Objects;

public record Coordinate(double latitude, double longitude) {

    @Override
    public boolean equals(final Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }
        final Coordinate other = (Coordinate) otherObject;
        return Double.compare(latitude, other.latitude) == 0
                && Double.compare(longitude, other.longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return getClass().getName() + "[latitude = " + latitude + ", longitude = " + longitude + "]";
    }
}