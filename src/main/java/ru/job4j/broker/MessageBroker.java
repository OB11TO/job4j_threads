package ru.job4j.broker;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayDeque;
import java.util.Queue;

@Slf4j
@ThreadSafe
public class MessageBroker {

    private static final String MESSAGE_PRODUCED = "Message: {} is produced! ";
    private static final String MESSAGE_CONSUMER = "Message: {} is consumer! ";

    @GuardedBy("monitor")
    private final Queue<Message> messages;
    private final Object monitor = new Object();
    @Getter
    private final int maxStoredMessages;

    public MessageBroker(int maxStoredMessages) {
        this.messages = new ArrayDeque<>(maxStoredMessages);
        this.maxStoredMessages = maxStoredMessages;
    }

    public void produce(Message message) {
        synchronized (monitor) {
            while (messages.size() >= maxStoredMessages) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            this.messages.add(message);
            log.info(MESSAGE_PRODUCED, message);
            monitor.notifyAll();
        }
    }

    public Message consumer() {
        synchronized (monitor) {
            while (messages.isEmpty()) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Message message = this.messages.poll();
            log.info(MESSAGE_CONSUMER, message);
            monitor.notifyAll();
            return message;
        }
    }

}
