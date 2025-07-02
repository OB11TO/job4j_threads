package ru.job4j.concurrent.collection.concurrenthashmap.cache;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CacheTest {
    @Test
    public void whenAddFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        var find = cache.findById(base.id());
        assertThat(find.get().name())
                .isEqualTo("Base");
    }

    @Test
    public void whenAddUpdateFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(new Base(1, "Base updated", 1));
        var find = cache.findById(base.id());
        assertThat(find.get().name())
                .isEqualTo("Base updated");
    }

    @Test
    public void whenAddDeleteFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.delete(1);
        var find = cache.findById(base.id());
        assertThat(find.isEmpty()).isTrue();
    }

    @Test
    public void whenMultiUpdateThrowException() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(base);
        assertThatThrownBy(() -> cache.update(base))
                .isInstanceOf(OptimisticException.class);
    }

    @Test
    public void whenMultiDelete() {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.delete(1);
        assertThat(cache.findById(base.id())).isEmpty();

    }

    @Test
    public void whenMultipleUpdatesThenVersionIncrements() {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);

        boolean firstUpdated = cache.update(base);
        var afterFirst = cache.findById(base.id()).orElseThrow();
        boolean secondUpdated = cache.update(afterFirst);
        var result = cache.findById(base.id()).orElseThrow();

        assertThat(firstUpdated).isTrue();
        assertThat(secondUpdated).isTrue();
        assertThat(result.version())
                .isEqualTo(3);
    }
}