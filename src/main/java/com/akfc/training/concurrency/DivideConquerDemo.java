package com.akfc.training.concurrency;


import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class DivideConquerDemo {

    public static void main(String[] argv) {
        long total = 0;
        long start = System.currentTimeMillis();
        for (long i = 0; i <= 1_000_000; i++) {
            total += i;
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println(total + " computed in " + duration + " ms");
        ForkJoinPool pool = ForkJoinPool.commonPool();
        start = System.currentTimeMillis();
        total = pool.invoke(new RecursiveSum(0, 1_000_000));
        duration = System.currentTimeMillis() - start;
        pool.shutdown();
        System.out.println(total + " computed in " + duration + " ms");
    }

    static class RecursiveSum extends RecursiveTask<Long> {

        private long lo, hi;

        public RecursiveSum(long lo, long hi) {
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected Long compute() {
            if (hi - lo <= 1000) {
                long total = 0;
                for (long i = lo; i <= hi; i++) {
                    total += i;
                }
                return total;
            } else {
                long mid = (hi + lo) / 2;
                RecursiveSum left = new RecursiveSum(lo, mid);
                RecursiveSum right = new RecursiveSum(mid+1, hi);
                left.fork();
                return right.compute() + left.join();
            }
        }
    }
}
