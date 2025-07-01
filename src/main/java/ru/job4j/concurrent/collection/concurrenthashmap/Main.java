package ru.job4j.concurrent.collection.concurrenthashmap;

public class Main {
    public static void main(String[] args) {
        CounterTestUtil.test(new SingleThreadLetterCounter());
        CounterTestUtil.test(new MultiThreadLetterCounter(4));
    }
}
