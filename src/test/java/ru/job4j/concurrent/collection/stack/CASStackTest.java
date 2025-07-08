package ru.job4j.concurrent.collection.stack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class CASStackTest {

    private CASStack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new CASStack<>();
    }

    @Test
    void pushAndPopSingleElement() {
        Integer value = 42;
        stack.push(value);
        Optional<Integer> result = stack.pop();
        assertThat(result)
                .isPresent()
                .contains(value);
    }

    @Test
    void popEmptyStackReturnsEmpty() {
        Optional<Integer> result = stack.pop();
        assertThat(result)
                .isEmpty();
    }

    @Test
    void pushMultiplePopAllInReverseOrder() {

        List<Integer> input = List.of(1, 2, 3, 4, 5);
        input.forEach(stack::push);

        List<Integer> output = new ArrayList<>();
        for (Optional<Integer> popped = stack.pop(); popped.isPresent(); popped = stack.pop()) {
            output.add(popped.get());
        }
        assertThat(output)
                .containsExactly(5, 4, 3, 2, 1);
    }

    @Test
    void concurrentPushAndPop() throws InterruptedException {
        int threads = 10;
        int pushesPerThread = 1000;
        ExecutorService exec = Executors.newFixedThreadPool(threads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            final int base = t * pushesPerThread;
            exec.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < pushesPerThread; i++) {
                        stack.push(base + i);
                    }
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await();
        exec.shutdown();

        Set<Integer> popped = ConcurrentHashMap.newKeySet();
        for (Optional<Integer> opt = stack.pop(); opt.isPresent(); opt = stack.pop()) {
            popped.add(opt.get());
        }
        assertThat(popped)
                .hasSize(threads * pushesPerThread)
                .containsAll(IntStream.range(0, threads * pushesPerThread)
                        .boxed()
                        .toList());
    }
}