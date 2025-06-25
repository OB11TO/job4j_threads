package ru.job4j.concurrent.blocking.semaphore;

import java.util.function.Supplier;

public final class ConnectionPool extends AbstractPool<Connection> {

    public ConnectionPool(int size) {
        super(new ConnectionSupplier(), size);
    }

    private static final class ConnectionSupplier implements Supplier<Connection> {

        private int nextNumberId;

        @Override
        public Connection get() {
            return new Connection(nextNumberId++, true);
        }
    }

    @Override
    protected void cleanObject(Connection value) {
        value.setAutoCommit(true);
    }
}
