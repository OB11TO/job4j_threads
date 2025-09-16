package ru.job4j.concurrent.future.city.model;

import java.util.Objects;

public record Area(Coordinate leftBottom, Coordinate rightUpper) {

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
        final Area other = (Area) otherObject;
        return Objects.equals(leftBottom, other.leftBottom) && Objects.equals(rightUpper, other.rightUpper);
    }

    @Override
    public String toString() {
        return getClass().getName() + "[leftBottom = " + leftBottom + ", rightUpper = " + rightUpper + "]";
    }
}