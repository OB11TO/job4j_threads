package ru.job4j.concurrent.blocking.countdownlatch;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Runner {
    public static void main(String[] args) {
        final int resourcesCount = 3;
        CountDownLatch countDownLatch = new CountDownLatch(resourcesCount);
        Thread[] resourceThreadsLoader = createResourceThreads(ResourceLoaderFactory::new, resourcesCount, countDownLatch);

        final int handlingThreadsCount = 4;
        Thread[] resourceThreadsHandler = createResourceThreads(ResourceHandlerFactory::new, handlingThreadsCount, countDownLatch);

        startToRunThreads(resourceThreadsLoader);
        startToRunThreads(resourceThreadsHandler);

    }

    private static void startToRunThreads(Thread[] threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }

    private static Thread[] createResourceThreads(final Supplier<ResourceFactory> taskFactorySupplier,
                                                  final int threadsCount,
                                                  final CountDownLatch latch) {
        final ResourceFactory taskFactory = taskFactorySupplier.get();
        return IntStream.range(0, threadsCount)
                .mapToObj(i -> taskFactory.create(latch))
                .map(Thread::new)
                .toArray(Thread[]::new);
    }
}
