package ru.job4j.concurrent.blocking.exchanger.example;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExchangerDebugExample {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        /* Поток A — “Producer” */
        new Thread(() -> {
            String myData = "Буфер-A"; /* 🏷️ то, что A готов отдать */
            try {
                System.out.println("A: перед exchange(), myData = " + myData);
                /*  🔄 Здесь A встретится с B и обменяется данными: */
                String fromB = exchanger.exchange(myData, 5, TimeUnit.SECONDS);
                System.out.println("A: после exchange(), получил fromB = " + fromB);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (TimeoutException e) {
                System.out.println("A: потерял встречу (timeout)!");
            }
        }, "Thread-A").start();

        /* Поток B — “Consumer”*/
        new Thread(() -> {
            String myData = "Буфер-B"; /* 🏷️ то, что B готов отдать */
            try {
                /* Ждём чуть дольше, чтобы показать, что A блокируется до прихода B: */
                Thread.sleep(100);
                System.out.println("B: перед exchange(), myData = " + myData);
                String fromA = exchanger.exchange(myData, 5, TimeUnit.SECONDS);
                System.out.println("B: после exchange(), получил fromA = " + fromA);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (TimeoutException e) {
                System.out.println("B: потерял встречу (timeout)!");
            }
        }, "Thread-B").start();
    }
}
