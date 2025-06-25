package ru.job4j.concurrent.blocking.semaphore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public final class Connection {
    private final long id;
    @Setter
    private boolean autoCommit;

    public Connection(long id, boolean autoCommit) {
        this.id = id;
        this.autoCommit = autoCommit;
    }
}
