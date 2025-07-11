package ru.job4j.concurrent.pool.email;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EmailNotification {
    private final ExecutorService executorService;

    public EmailNotification(int poolSize) {
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    public void emailTo(User user) {
        executorService.submit(() -> {
            String subject = String.format("Notification %s to email %s", user.getUsername(), user.getEmail());
            String body = String.format("Add a new event to %s", user.getUsername());
            send(subject, body, user.getEmail());
        });
    }

    public void send(String subject, String body, String email) {

    }

    public void close() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for executor shutdown", e);
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}

@Getter
class User {
    private final String username;
    private final String email;

    User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
