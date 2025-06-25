package ru.job4j.concurrent.blocking.cyclibarrier;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public abstract class Task {
    private final long id;

    public Task(long id) {
        this.id = id;
    }

    public abstract void perform();
}
