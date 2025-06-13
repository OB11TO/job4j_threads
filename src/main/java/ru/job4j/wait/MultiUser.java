package ru.job4j.wait;

import lombok.SneakyThrows;

public class MultiUser {
    @SneakyThrows
    public static void main(String[] args) {
        Barrier barrier = new Barrier();
        Thread master = new Thread(
                () -> {
                    System.out.println(Thread.currentThread().getName() + " started");
                    barrier.on();
                },
                "Master"
        );
        Thread slave = new Thread(
                () -> {
                    barrier.check();
                    System.out.println(Thread.currentThread().getName() + " started");
                },
                "Slave"
        );
        slave.start();
        Thread.sleep(500L);
        master.start();
    }
}