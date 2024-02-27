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
        sort(0, array.length-1);
        return array;
    }

    /* helper method that gets called recursively */
    private void sort(int left, int right) {
        if (left < right) {
            int mid = (left+right)/2; // find the middle point
            sort(left, mid); // sort the left half
            sort(mid+1, right); // sort the right half
            merge(left, mid, right); // merge the two sorted halves
        }
    }

    /* helper method to merge two sorted subarrays array[l..m] and array[m+1..r] into array */
    private void merge(int left, int mid, int right) {
        // copy data to temp subarrays to be merged
        int leftTempArray[] = Arrays.copyOfRange(array, left, mid+1);
        int rightTempArray[] = Arrays.copyOfRange(array, mid+1, right+1);

        // initial indexes for left, right, and merged subarrays
        int leftTempIndex=0, rightTempIndex=0, mergeIndex=left;

        // merge temp arrays into original
        while (leftTempIndex < mid - left + 1 || rightTempIndex < right - mid) {
            if (leftTempIndex < mid - left + 1 && rightTempIndex < right - mid) {
                if (leftTempArray[leftTempIndex] <= rightTempArray[rightTempIndex]) {
                    array[mergeIndex ] = leftTempArray[leftTempIndex];
                    leftTempIndex++;
                } else {
                    array[mergeIndex ] = rightTempArray[rightTempIndex];
                    rightTempIndex++;
                }
            } else if (leftTempIndex < mid - left + 1) { // copy any remaining on left side
                array[mergeIndex ] = leftTempArray[leftTempIndex];
                leftTempIndex++;
            } else if (rightTempIndex < right - mid) { // copy any remaining on right side
                array[mergeIndex ] = rightTempArray[rightTempIndex];
                rightTempIndex++;
            }
            mergeIndex++;
        }
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
        int numWorkers = Runtime.getRuntime().availableProcessors();
        ForkJoinPool pool = new ForkJoinPool(numWorkers);
        pool.invoke(new ParallelWorker(0,array.length-1));
        return array;
    }

    /* worker that gets called recursively */
    private class ParallelWorker extends RecursiveAction {

        private int left, right;

        public ParallelWorker(int left, int right) {
            this.left = left;
            this.right = right;
        }

        protected void compute() {
            if (left < right) {
                int mid = (left + right) / 2; // find the middle point
                ParallelWorker leftWorker = new ParallelWorker(left, mid);
                ParallelWorker rightWorker = new ParallelWorker(mid+1, right);
                invokeAll(leftWorker, rightWorker);
                merge(left, mid, right); // merge the two sorted halves
            }
        }
    }

    /* helper method to merge two sorted subarrays array[l..m] and array[m+1..r] into array */
    private void merge(int left, int mid, int right) {
        // copy data to temp subarrays to be merged
        int leftTempArray[] = Arrays.copyOfRange(array, left, mid+1);
        int rightTempArray[] = Arrays.copyOfRange(array, mid+1, right+1);

        // initial indexes for left, right, and merged subarrays
        int leftTempIndex=0, rightTempIndex=0, mergeIndex=left;

        // merge temp arrays into original
        while (leftTempIndex < mid - left + 1 || rightTempIndex < right - mid) {
            if (leftTempIndex < mid - left + 1 && rightTempIndex < right - mid) {
                if (leftTempArray[leftTempIndex] <= rightTempArray[rightTempIndex]) {
                    array[mergeIndex ] = leftTempArray[leftTempIndex];
                    leftTempIndex++;
                } else {
                    array[mergeIndex ] = rightTempArray[rightTempIndex];
                    rightTempIndex++;
                }
            } else if (leftTempIndex < mid - left + 1) { // copy any remaining on left side
                array[mergeIndex ] = leftTempArray[leftTempIndex];
                leftTempIndex++;
            } else if (rightTempIndex < right - mid) { // copy any remaining on right side
                array[mergeIndex ] = rightTempArray[rightTempIndex];
                rightTempIndex++;
            }
            mergeIndex++;
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
