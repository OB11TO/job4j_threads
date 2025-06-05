package ru.job4j.volatiles;

public class VolatileDemo {

    private static int flag = 1;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (flag == 1) {
                System.out.println("WW");
            }
        });
        thread.start();
        Thread.sleep(5000);
        Thread thread1 = new Thread(() -> {
            flag = 0;
            System.out.println("GG");
        });
        thread1.start();
    }
}
