package fr.cenotelie.training.concurrency;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

class SequentialMergeSorter {

    private int[] array;

    public SequentialMergeSorter(int[] array) {
        this.array = array;
    }

    /* returns sorted array */
    public int[] sort() {
        //TODO
        return null;
    }

    /* helper method that gets called recursively */
    private void sort(int left, int right) {
        //TODO
    }

    /* helper method to merge two sorted subarrays array[l..m] and array[m+1..r] into array */
    private void merge(int left, int mid, int right) {
        //TODO
    }
}

/* parallel implementation of merge sort */
class ParallelMergeSorter {

    private int[] array;

    public ParallelMergeSorter(int[] array) {
        this.array = array;
    }

    /* returns sorted array */
    public int[] sort() {
        //TODO
        return null;
    }

    /* worker that gets called recursively */
    private class ParallelWorker extends RecursiveAction {

        @Override
        protected void compute() {
            //TODO
        }
    }
}
public class MergeSort {

    static int[] generateRandomArray(int length) {
        System.out.format("Generating random array int[%d]...\n", length);
        Random rand = new Random();
        int[] output = new int[length];
        for (int i=0; i<length; i++)
            output[i] = rand.nextInt();
        return output;
    }

    public static void main(String[] args) {
        final int NUM_EVAL_RUNS = 5;
        final int[] input = generateRandomArray(100_000_000);

        System.out.println("Evaluating Sequential Implementation...");
        SequentialMergeSorter sms = new SequentialMergeSorter(Arrays.copyOf(input, input.length));
        int[] sequentialResult = sms.sort();
        double sequentialTime = 0;
        for(int i=0; i<NUM_EVAL_RUNS; i++) {
            sms = new SequentialMergeSorter(Arrays.copyOf(input, input.length));
            long start = System.currentTimeMillis();
            sms.sort();
            sequentialTime += System.currentTimeMillis() - start;
        }
        sequentialTime /= NUM_EVAL_RUNS;

        System.out.println("Evaluating Parallel Implementation...");
        ParallelMergeSorter pms = new ParallelMergeSorter(Arrays.copyOf(input, input.length));
        int[] parallelResult = pms.sort();
        double parallelTime = 0;
        for(int i=0; i<NUM_EVAL_RUNS; i++) {
            pms = new ParallelMergeSorter(Arrays.copyOf(input, input.length));
            long start = System.currentTimeMillis();
            pms.sort();
            parallelTime += System.currentTimeMillis() - start;
        }
        parallelTime /= NUM_EVAL_RUNS;

        // display sequential and parallel results for comparison
        if (!Arrays.equals(sequentialResult, parallelResult))
            throw new Error("ERROR: sequentialResult and parallelResult do not match!");
        System.out.format("Average Sequential Time: %.1f ms\n", sequentialTime);
        System.out.format("Average Parallel Time: %.1f ms\n", parallelTime);
        System.out.format("Speedup: %.2f \n", sequentialTime/parallelTime);
        System.out.format("Efficiency: %.2f%%\n", 100*(sequentialTime/parallelTime)/Runtime.getRuntime().availableProcessors());
    }

}
