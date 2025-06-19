package ru.job4j.core.concurrent;

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

        try {
            first.join();
            second.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Work of the threads is completed");
        statusThread(first);
        statusThread(second);
    }

    private static void statusThread(Thread thread) {
        System.out.println(thread.getName() + " " + thread.getState());
    }
}