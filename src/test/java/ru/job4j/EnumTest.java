package ru.job4j;

public enum EnumTest {
    ADD("add"),
    DELETE("delete"),
    UPDATE("update"),
    VIEW("view");

    private final String command;

    EnumTest(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void method() {
         enum LocalEnum { A, B, C } // СРАБОТАЕТ !!!!!!
    }

}