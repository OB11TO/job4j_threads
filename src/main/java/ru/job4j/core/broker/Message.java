package ru.job4j.core.broker;

import java.util.Objects;

public final class Message {
    private final String data;

    public Message(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object massage) {
        if (this == massage) {
            return true;
        }
        if (massage == null || getClass() != massage.getClass()) {
            return false;
        }
        Message that = (Message) massage;
        return Objects.equals(this.data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[data = " + this.data + "]";
    }
}
