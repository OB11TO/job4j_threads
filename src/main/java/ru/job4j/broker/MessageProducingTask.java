package ru.job4j.broker;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MessageProducingTask implements Runnable {

    private static final int SLEEP = 1;
    private static final String MESSAGE_PRODUCED = "Message: {} is produced! ";
    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;

    @Setter
    @Getter
    private int minimumMSGToStartProduce;
    @Getter
    private final String name;

    public MessageProducingTask(MessageBroker messageBroker, final MessageFactory messageFactory, final int minimumMSGToStartProduce, final String name) {
        this.messageBroker = messageBroker;
        this.messageFactory = messageFactory;
        this.minimumMSGToStartProduce = minimumMSGToStartProduce;
        this.name = name;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Message message = messageFactory.create();
            TimeUnit.SECONDS.sleep(SLEEP);
            messageBroker.produce(message);
            log.info(MESSAGE_PRODUCED, message);
        }
    }
}
