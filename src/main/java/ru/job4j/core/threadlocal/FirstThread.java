package ru.job4j.core.threadlocal;

public class FirstThread extends Thread {

    @Override
    public void run() {
        ThreadLocalDemo.threadLocal.set("Это поток 1.");
        System.out.println(ThreadLocalDemo.threadLocal.get());
    }
}