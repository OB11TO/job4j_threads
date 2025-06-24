package ru.job4j.concurrent.semaphore;

import java.util.function.Supplier;

public final class ConnectionPool extends AbstractPool<Connection> {

    private static final class ConnectionSupplier implements Supplier<Connection> {
        private long nextConnectionId;

        @Override
        public Connection get() {
            return new Connection(this.nextConnectionId++, true);
        }
    }


    public ConnectionPool(int size) {
        super(new ConnectionSupplier(), size);
    }

    @Override
    protected void cleanObject(Connection value) {
        value.setAutoCommit(true);
    }
}
