package ru.job4j.core.singleton;

public class BaseSingleton {

    private static class SingletonHolder {
        static final BaseSingleton INSTANCE = new BaseSingleton();
    }

    public static BaseSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
