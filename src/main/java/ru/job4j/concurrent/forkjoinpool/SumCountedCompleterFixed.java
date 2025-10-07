package ru.job4j.concurrent.forkjoinpool;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SumCountedCompleterFixed extends CountedCompleter<Long> {
    static final int THRESHOLD = 500;
    final long[] arr;
    final int lo, hi;
    volatile long result;
    SumCountedCompleterFixed left, right;

    public SumCountedCompleterFixed(CountedCompleter<?> parent, long[] arr, int lo, int hi) {
        super(parent);
        this.arr = arr;
        this.lo = lo;
        this.hi = hi;
    }

    private void log(String msg) {
        String time = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        System.out.printf("%s [%s] %s%n", time, Thread.currentThread().getName(), msg);
    }

    @Override
    public void compute() {
        int len = hi - lo;
        if (len <= THRESHOLD) {
            // листовая задача
            long s = 0;
            for (int i = lo; i < hi; i++) {
                s += arr[i];
            }
            this.result = s;
            setRawResult(s);
            log("LEAF [" + lo + "," + hi + ") sum=" + s + " -> tryComplete()");
        } else {
            int mid = lo + len / 2;
            left = new SumCountedCompleterFixed(this, arr, lo, mid);
            right = new SumCountedCompleterFixed(this, arr, mid, hi);

            // ставим pending ДО fork
            addToPendingCount(2);
            log("SPLIT [" + lo + "," + hi + ") -> pending=2");

            // запускаем детей
            left.fork();
            right.fork();
            // compute() сразу возвращается, поток свободен
        }
        tryComplete(); // важно: сигнал родителю
    }

    @Override
    public void onCompletion(CountedCompleter<?> caller) {
        if (left != null && right != null) {
            this.result = left.result + right.result;
            setRawResult(this.result);
            log("onCompletion [" + lo + "," + hi + ") = " + this.result);
        }
    }

    @Override
    public Long getRawResult() {
        return result;
    }

    public static void main(String[] args) {
        int n = 2000;
        long[] arr = new long[n];
        for (int i = 0; i < n; i++) {
            arr[i] = ThreadLocalRandom.current().nextInt(1, 10);
        }

        SumCountedCompleterFixed root = new SumCountedCompleterFixed(null, arr, 0, arr.length);
        try (ForkJoinPool pool = ForkJoinPool.commonPool()) {

            System.out.println("=== START ===");
            long start = System.currentTimeMillis();
            pool.invoke(root); // main ждёт root
            long end = System.currentTimeMillis();

            System.out.println("=== FINISH result=" + root.getRawResult() + " time=" + (end - start) + "ms ===");

        }
    }
}
