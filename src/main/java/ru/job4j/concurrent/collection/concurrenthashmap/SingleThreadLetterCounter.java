package ru.job4j.concurrent.collection.concurrenthashmap;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SingleThreadLetterCounter extends LetterCounter {

    public SingleThreadLetterCounter() {
        super(1);
    }

    @Override
    protected void executeRunner(Stream<Subtask> subtasks) {
        subtasks.forEach(Subtask::execute);
    }

    @Override
    protected Map<Character, Integer> createAccumulator() {
        return new HashMap<>();
    }
}

