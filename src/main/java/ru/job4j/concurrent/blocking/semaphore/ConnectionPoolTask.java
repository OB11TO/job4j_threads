package ru.job4j.concurrent.blocking.semaphore;

import java.util.concurrent.TimeUnit;

public class ConnectionPoolTask extends AbstractPoolTask<Connection> {

    protected ConnectionPoolTask(AbstractPool<Connection> abstractPool) {
        super(abstractPool);
    }

    @Override
    public void handle(Connection object) {
        try {
            object.setAutoCommit(false);
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
