package ru.job4j.concurrent.collection.concurrenthashmap;

import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class LetterCounter {

    private final int subtaskCount;

    public LetterCounter(int subtaskCount) {
        this.subtaskCount = subtaskCount;
    }

    public Map<Character, Integer> count(String inputText) {
        Map<Character, Integer> accumulator = createAccumulator();
        Stream<Subtask> subtasks = createSubtasks(accumulator, inputText);
        executeRunner(subtasks);
        return accumulator;
    }

    protected abstract void executeRunner(Stream<Subtask> subtasks);

    private Stream<Subtask> createSubtasks(Map<Character, Integer> accumulator, String inputText) {
        int subtaskCharCount = findSubtaskCharCount(inputText);
        return IntStream.range(0, subtaskCount).mapToObj(i -> createSubtasks(accumulator, inputText, subtaskCharCount, i));
    }

    private Subtask createSubtasks(Map<Character, Integer> accumulator, String inputText, int subtaskCharCount, int index) {
        int start = index * subtaskCharCount;
        int end = Math.min((index + 1) * subtaskCharCount, inputText.length());
        return new Subtask(accumulator, inputText, start, end);
    }

    private int findSubtaskCharCount(String inputText) {
        return (int) Math.ceil((double) inputText.length() / subtaskCount);
    }

    protected abstract Map<Character, Integer> createAccumulator();

    protected record Subtask(Map<Character, Integer> accumulator, String inputText, int start, int end) {

        public void execute() {
            IntStream.range(start, end)
                    .map(inputText::codePointAt)
                    .filter(Character::isLetter)
                    .map(Character::toLowerCase)
                    .forEach(this::accumulate);
        }

        private void accumulate(int codePoint) {
            Character character = (char) codePoint;
            accumulator.merge(character, 1, Integer::sum);
        }
    }
}
