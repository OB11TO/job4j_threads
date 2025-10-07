package ru.job4j.concurrent.future.city.service;

import ru.job4j.concurrent.future.city.model.Area;
import ru.job4j.concurrent.future.city.model.City;
import ru.job4j.concurrent.future.city.model.Coordinate;
import ru.job4j.concurrent.future.city.ulit.FutureUtil;
import ru.job4j.concurrent.future.city.ulit.IteratorUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.ceil;
import static java.lang.Math.min;
import static java.util.Arrays.asList;

public class CityScanner {

    private static final int TASK_POINT_COUNT = 5;

    private final ExecutorService executorService;
    private final CityClient cityClient;

    public CityScanner(CityClient cityClient, ExecutorService executorService) {
        this.cityClient = cityClient;
        this.executorService = executorService;
    }

    public Set<City> scan(Area area) {
        final List<Coordinate> coordinates = IteratorUtil.asList(new AreaIterator(area));
        return IntStream.range(0, countTasks(coordinates))
                .mapToObj(i -> getTaskCoordinates(coordinates, i))
                .map(ScanningTask::new)
                .map(executorService::submit)
                .toList()
                .stream()
                .map(FutureUtil::get)
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableSet());
    }

    private List<Coordinate> getTaskCoordinates(final List<Coordinate> coordinates, final int taskIndex) {
        final int fromIndex = taskIndex * TASK_POINT_COUNT;
        final int toIndex = min(TASK_POINT_COUNT * (taskIndex + 1), coordinates.size());
        return coordinates.subList(fromIndex, toIndex);
    }

    private int countTasks(final List<Coordinate> coordinates) {
        return (int) ceil(((double) coordinates.size()) / TASK_POINT_COUNT);
    }

    private final class ScanningTask implements Callable<Set<City>> {

        private final List<Coordinate> coordinates;

        private ScanningTask(List<Coordinate> coordinates) {
            this.coordinates = coordinates;
        }

        @Override
        public Set<City> call() {
            return coordinates.stream()
                    .map(cityClient::request)
                    .collect(Collectors.toCollection(HashSet::new));
        }
    }

}
