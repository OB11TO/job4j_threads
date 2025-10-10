package ru.job4j.concurrent.forkjoinpool;

import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class ParallelSearchIndexArray<T> extends RecursiveTask<Integer> {

    public static final int THRESHOLD = 10;

    private final T[] array;
    private final T target;
    private final int firstIndex;
    private final int lastIndex;

    public ParallelSearchIndexArray(T[] array, T target, int firstIndex, int lastIndex) {
        this.array = array;
        this.target = target;
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
    }

    @Override
    protected Integer compute() {
        if (lastIndex - firstIndex <= THRESHOLD) {
            return IntStream.range(firstIndex, lastIndex)
                    .filter(i -> array[i].equals(target))
                    .findFirst()
                    .orElse(-1);
        }

        int middle = (firstIndex + lastIndex) / 2;
        ParallelSearchIndexArray<T> left = new ParallelSearchIndexArray<>(array, target, firstIndex, middle);
        ParallelSearchIndexArray<T> right = new ParallelSearchIndexArray<>(array, target, middle, lastIndex);

        left.fork();
        Integer rightResult = right.compute();
        Integer leftResult = left.join();

        return leftResult != -1 ? leftResult : rightResult;
    }
}
