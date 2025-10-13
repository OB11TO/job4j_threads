package ru.job4j.concurrent.completablefuture;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.job4j.concurrent.completablefuture.RolColSum.asyncSum;
import static ru.job4j.concurrent.completablefuture.RolColSum.sum;

class RolColSumTest {

    @Test
    public void whenSumsEmpty() {
        int[][] matrix = {};
        assertThat(sum(matrix)).isEmpty();
    }

    @Test
    public void whenSumsSize1() {
        int[][] matrix = {{2}};
        RolColSum.Sums sums = new RolColSum.Sums(2, 2);
        assertThat(sum(matrix)).containsExactly(sums);
    }

    @Test
    public void whenSumsSize2() {
        int[][] matrix = {{1, 2}, {4, 5}};
        assertThat(sum(matrix)).containsExactly(new RolColSum.Sums(3, 5),
                new RolColSum.Sums(9, 7));
    }

    @Test
    public void whenSumsSize3() {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        assertThat(sum(matrix)).containsExactly(new RolColSum.Sums(6, 12),
                new RolColSum.Sums(15, 15),
                new RolColSum.Sums(24, 18));
    }

    @Test
    public void whenAsyncSumsEmpty() {
        int[][] matrix = {};
        assertThat(asyncSum(matrix)).isEmpty();
    }

    @Test
    public void whenAsyncSumsSize1() {
        int[][] matrix = {{2}};
        assertThat(asyncSum(matrix)).containsExactly(new RolColSum.Sums(2, 2));
    }

    @Test
    public void whenAsyncSumsSize2() {
        int[][] matrix = {{1, 2}, {4, 5}};
        assertThat(asyncSum(matrix)).containsExactly(new RolColSum.Sums(3, 5),
                new RolColSum.Sums(9, 7));
    }

    @Test
    public void whenAsyncSumsSize3() {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        assertThat(asyncSum(matrix)).containsExactly(new RolColSum.Sums(6, 12),
                new RolColSum.Sums(15, 15),
                new RolColSum.Sums(24, 18));
    }
}