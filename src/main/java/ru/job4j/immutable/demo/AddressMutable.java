package ru.job4j.immutable.demo;

public class AddressMutable {
    private String city;
    private String street;

    public AddressMutable(String city, String street) {
        this.city = city;
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public AddressMutable copy() {
        return new AddressMutable(this.city, this.street);
    }
}

