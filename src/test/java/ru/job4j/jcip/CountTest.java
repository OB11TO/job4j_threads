package ru.job4j.jcip;

import org.junit.jupiter.api.Test;
import ru.job4j.core.jcip.Count;

import static org.assertj.core.api.Assertions.assertThat;

public class CountTest {

    @Test
    public void whenExecute2ThreadThen2() throws InterruptedException {
        var count = new Count();
        var first = new Thread(count::increment);
        var second = new Thread(count::increment);
        /* Запускаем нити. */
        first.start();
        second.start();
        /* Заставляем главную нить дождаться выполнения наших нитей. */
        first.join();
        second.join();
        /* Проверяем результат. НО ОН БУДЕТ НЕ ВСЕГДА ТАКОЙ, ЕСЛИ МЫ НЕ ПРАВИЛЬНО РАБОТАЕМ С МНОГОПОТОЧКОЙ */
        assertThat(count.get()).isEqualTo(2);
    }
}
