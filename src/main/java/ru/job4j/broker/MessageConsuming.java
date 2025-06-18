package ru.job4j.broker;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MessageConsuming implements Runnable {

    private static final int SLEEP = 5;

    private final MessageBroker messageBroker;
    @Getter
    private final int minimalMSGAmountToStart;
    @Getter
    private final String name;

    public MessageConsuming(MessageBroker messageBroker, int minimalMSGAmountToStart, final String name) {
        this.messageBroker = messageBroker;
        this.minimalMSGAmountToStart = minimalMSGAmountToStart;
        this.name = name;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            TimeUnit.SECONDS.sleep(SLEEP);
            messageBroker.consumer();
        }
    }
}
