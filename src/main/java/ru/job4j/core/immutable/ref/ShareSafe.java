package ru.job4j.core.immutable.ref;

public class ShareSafe {
    public static void main(String[] args) throws InterruptedException {
        UserCache cache = new UserCache();
        User user = User.of("main");
        cache.add(user);

        Thread first = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                user.setName("first");
            }
            cache.findAll().forEach(u -> System.out.println(u.getName()));
        });

        Thread second = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                user.setName("second");
            }
            cache.findAll().forEach(u -> System.out.println(u.getName()));
        });

        first.start();
        second.start();
        first.join();
        second.join();

        System.out.println(cache.findById(1).getName());

        cache.findAll().forEach(u -> System.out.println(u.getName()));
    }
}
