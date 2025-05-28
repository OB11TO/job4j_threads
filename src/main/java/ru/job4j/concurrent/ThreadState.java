package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> {
                }
        );
        Thread second = new Thread(
                () -> {
                }
        );
        statusThread(first);
        statusThread(second);

        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED
                && second.getState() != Thread.State.TERMINATED) {
            statusThread(first);
            statusThread(second);
        }
        System.out.println("Work of the threads is completed");
    }

    private static void statusThread(Thread thread) {
        System.out.println(thread.getName() + " " + thread.getState());
    }
}