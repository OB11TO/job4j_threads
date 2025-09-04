package ru.job4j.concurrent.collection.concurrenthashmap.cache;

import java.util.*;
import java.util.concurrent.*;

public class FailSafeDemo {
    public static void main(String[] args) {
        // failSafeHashMap();
        failSafeCopyOnWriteArrayList();

       // failFastArrayList();
       // failFastHashMap();
    }

    private static void failSafeCopyOnWriteArrayList() {
        CopyOnWriteArrayList<String> cow = new CopyOnWriteArrayList<>(new String[]{"A","B"});
        for (String s : cow) {
            System.out.println("iter: " + s);
            cow.add("C"); // не бросит, но текущий итератор не увидит "C"
            cow.add("D"); // не бросит, но текущий итератор не увидит "D"
        }
        System.out.println("final: " + cow); // содержит C D C D
    }


    private static void failFastHashMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "A");
        map.put(2, "B");

        for (Map.Entry<Integer, String> e : map.entrySet()) {
            System.out.println(e);
            map.put(3, "C"); // → ConcurrentModificationException
        }
    }

    private static void failFastArrayList() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2));
        for (Integer i : list) {
            System.out.println(i);
            list.add(3); // → ConcurrentModificationException
        }
    }

    private static void failSafeHashMap() {
        Map<Integer, String> cmap = new ConcurrentHashMap<>();
        cmap.put(1, "A");
        cmap.put(2, "B");

        for (Map.Entry<Integer, String> entry : cmap.entrySet()) {
            System.out.println(entry);

            // 🚀 Можно модифицировать во время итерации
            cmap.put(3, "C"); // не упадёт!
        }
        System.out.println("Итоговая карта: " + cmap);
    }

}
