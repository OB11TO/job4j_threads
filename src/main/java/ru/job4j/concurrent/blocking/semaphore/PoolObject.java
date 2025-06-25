package ru.job4j.concurrent.blocking.semaphore;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public final class PoolObject<T> {
    private final T value;
    @Setter
    private boolean issued;

    public PoolObject(T value, boolean isIssued) {
        this.value = value;
        this.issued = isIssued;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PoolObject<?> that = (PoolObject<?>) object;
        return issued == that.issued && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(value);
        result = 31 * result + Boolean.hashCode(issued);
        return result;
    }
}
