package ru.job4j.concurrent.future.city;

import ru.job4j.concurrent.future.city.model.Area;
import ru.job4j.concurrent.future.city.model.City;
import ru.job4j.concurrent.future.city.model.Coordinate;
import ru.job4j.concurrent.future.city.service.CityClient;
import ru.job4j.concurrent.future.city.service.CityScanner;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public final class Main {

    public static void main(final String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        try {
            CityScanner scanner = new CityScanner(new CityClient(), executorService);
            Area area = new Area(new Coordinate(1, 1), new Coordinate(5.3, 5.3));
            Set<City> cities = scanner.scan(area);
            System.out.println(cities);
        } finally {
            executorService.shutdown();
        }
    }
}