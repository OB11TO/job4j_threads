package ru.job4j.core.concurrent;

public class ConcurrentOutput {
    public static void main(String[] args) {
        Thread another = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        another.start();
        System.out.println(Thread.currentThread().getName());
        Thread second = new Thread(() -> System.out.println(Thread.currentThread().getName()));
        second.start();
    }
}