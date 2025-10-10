package ru.job4j.concurrent.forkjoinpool;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

import static org.assertj.core.api.Assertions.assertThat;

public class ParallelSearchIndexArrayTest {

    private <T> int invokeSearch(T[] array, T target) {
        ParallelSearchIndexArray<T> task = new ParallelSearchIndexArray<>(array, target, 0, array.length);
        return ForkJoinPool.commonPool().invoke(task);
    }

    @Test
    void smallArrayIntegerFoundAtStart() {
        Integer[] array = {1, 2, 3, 4, 5};
        assertThat(0).isEqualTo(invokeSearch(array, 1));
    }

    @Test
    void smallArrayIntegerFoundAtEnd() {
        Integer[] array = {1, 2, 3, 4, 5};
        assertThat(4).isEqualTo(invokeSearch(array, 5));
    }

    @Test
    void smallArrayFirstOccurrence() {
        Integer[] array = {7, 3, 3, 9, 3};
        assertThat(1).isEqualTo(invokeSearch(array, 3));
    }

    @Test
    void largeArrayIntegerFoundMiddle() {
        Integer[] array = new Integer[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        assertThat(50).isEqualTo(invokeSearch(array, 50));
    }

    @Test
    void largeArrayNotFound() {
        Integer[] array = new Integer[200];
        for (int i = 0; i < array.length; i++) {
            array[i] = i * 2; // only even numbers
        }
        assertThat(-1).isEqualTo(invokeSearch(array, 5000));
    }

    @Test
    void stringArraySearch() {
        String[] array = {"a", "b", "c", "d", "e"};
        assertThat(2).isEqualTo(invokeSearch(array, "c"));
    }

    @Test
    void customObjectSearch() {
        Person[] people = new Person[]{
                new Person(1, "Alice"),
                new Person(2, "Bob"),
                new Person(3, "Charlie"),
                new Person(4, "Bob")
        };
        assertThat(1).isEqualTo(invokeSearch(people, new Person(2, "Bob")));
    }


    private record Person(int id, String name) {

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Person person = (Person) o;
            return id == person.id && (Objects.equals(name, person.name));
        }

        @Override
        public int hashCode() {
            int result = Integer.hashCode(id);
            result = 31 * result + (name == null ? 0 : name.hashCode());
            return result;
        }
    }
}
