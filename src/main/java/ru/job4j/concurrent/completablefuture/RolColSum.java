package ru.job4j.concurrent.completablefuture;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class RolColSum {

    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    public static class Sums {
        private int rowSum;
        private int colSum;

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }
    }

    public static Sums[] sum(int[][] matrix) {
        int n = matrix.length;
        Sums[] result = new Sums[n];
        int[] rowSums = new int[n];
        int[] colSums = new int[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                rowSums[i] += matrix[i][j];
                colSums[j] += matrix[i][j];
            }
        }
        for (int i = 0; i < n; i++) {
            result[i] = new Sums(rowSums[i], colSums[i]);
        }
        return result;
    }

    public static Sums[] asyncSum(int[][] matrix) {
        List<CompletableFuture<Sums>> futures = IntStream.range(0, matrix.length)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    int rowSum = getRowMatrixSum(matrix, i);
                    int colSum = getColMatrixSum(matrix, i);
                    return new Sums(rowSum, colSum);
                }))
                .toList();
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toArray(Sums[]::new))
                .join();
    }

    private static int getRowMatrixSum(int[][] matrix, int i) {
        int sum = 0;
        for (int j = 0; j < matrix[i].length; j++) {
            sum += matrix[i][j];
        }
        return sum;
    }

    private static int getColMatrixSum(int[][] matrix, int i) {
        int sum = 0;
        for (int j = 0; j < matrix.length; j++) {
            sum += matrix[j][i];
        }
        return sum;
    }
}
