package ru.job4j.concurrent.blocking.exchanger.example;

import java.util.concurrent.Exchanger;

public class InMemoryServerClient {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        new Thread(() -> {
            try {
                while (true) {
                    String request = exchanger.exchange("READY");
                    System.out.println("Server получил: " + request);
                    exchanger.exchange("OK:" + request);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    exchanger.exchange("CMD" + i);
                    String response = exchanger.exchange("ACK");
                    System.out.println("Client получил: " + response);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
