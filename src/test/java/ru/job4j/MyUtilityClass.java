package ru.job4j;

public final class MyUtilityClass {

    private static long nextId = 4L;

    private MyUtilityClass() {
    }

    public static String returnUpperCase(String stringInput) {
        nextId = 5L;
        return stringInput.toUpperCase();
    }

    public static String returnLowerCase(String stringInput) {
        return stringInput.toLowerCase();
    }

    public static String[] splitStringInput(String stringInput, String delimiter) {
        return stringInput.split(delimiter);
    }

}