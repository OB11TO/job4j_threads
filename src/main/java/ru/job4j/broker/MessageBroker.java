package ru.job4j.broker;

import lombok.Getter;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayDeque;
import java.util.Queue;

@ThreadSafe
public class MessageBroker {

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
            monitor.notifyAll();
            this.messages.add(message);
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
            monitor.notifyAll();
            return this.messages.poll();
        }
    }

}
