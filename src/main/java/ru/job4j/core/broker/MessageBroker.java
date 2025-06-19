package ru.job4j.core.broker;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayDeque;
import java.util.Queue;

@Slf4j
@ThreadSafe
public class MessageBroker {

    private static final String MESSAGE_PRODUCED = "Message: {} is produced! Thread name is {} ! Amount of messages is {} ";
    private static final String MESSAGE_CONSUMER = "Message: {} is consumer! Thread name is {} ! Amount of messages is {} ";

    @GuardedBy("monitor")
    private final Queue<Message> messages;
    private final Object monitor = new Object();
    @Getter
    private final int maxStoredMessages;

    public MessageBroker(int maxStoredMessages) {
        this.messages = new ArrayDeque<>(maxStoredMessages);
        this.maxStoredMessages = maxStoredMessages;
    }

    public void produce(Message message, final MessageProducing messageProducing) {
        synchronized (monitor) {
            while (!this.isShouldProduce(messageProducing)) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            this.messages.add(message);
            log.info(MESSAGE_PRODUCED, message, messageProducing.getName(), messages.size());
            monitor.notifyAll();
        }
    }

    public Message consumer(final MessageConsuming messageConsumer) {
        synchronized (monitor) {
            while (!this.isShouldConsume(messageConsumer)) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Message message = this.messages.poll();
            log.info(MESSAGE_CONSUMER, message, messageConsumer.getName(), messages.size());
            monitor.notifyAll();
            return message;
        }
    }

    private boolean isShouldConsume(final MessageConsuming messageConsumer) {
        synchronized (monitor) {
            return !this.messages.isEmpty() && this.messages.size() >= messageConsumer.getMinimalMSGAmountToStart();
        }
    }

    private boolean isShouldProduce(final MessageProducing messageProducing) {
        synchronized (monitor) {
            return this.messages.size() < this.maxStoredMessages
                    && this.messages.size() <= messageProducing.getMinimumMSGToStartProduce();
        }
    }
}

