package ru.job4j.core.immutable.demo;

import java.util.*;

public final class Person {
    private final String name;
    private final AddressMutable addressMutable;
    private final List<String> phones;

    public Person(String name, AddressMutable addressMutable, List<String> phones) {
        this.name = name;
        this.addressMutable = addressMutable.copy();
        this.phones = List.copyOf(phones);
    }

    public String getName() {
        return name;
    }

    public AddressMutable getAddressMutable() {
        return addressMutable.copy();
    }

    public List<String> getPhones() {
        return List.copyOf(phones);
    }

    public Person withName(String newName) {
        return new Person(newName, this.addressMutable, this.phones);
    }

    public Person withAddressMutable(AddressMutable newAddressMutable) {
        return new Person(this.name, newAddressMutable.copy(), this.phones);
    }

    public Person withPhones(List<String> newPhones) {
        return new Person(this.name, this.addressMutable, List.copyOf(newPhones));
    }

    public Person withAddedPhone(String newPhone) {
        List<String> newList = new ArrayList<>(this.phones);
        newList.add(newPhone);
        return new Person(this.name, this.addressMutable, newList);
    }
}
