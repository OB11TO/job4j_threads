package ru.job4j.concurrent.future.city.model;

import java.util.Objects;

public record City(Long id) {

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
        final City other = (City) otherObject;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return getClass() + "[id = " + id + "]";
    }
}
