package com.akfc.training.concurrency;

import java.util.Random;
import java.util.concurrent.*;

public class MatrixProd {

    private int[][] A, B;
    private int numRowsA, numColsA, numRowsB, numColsB;

    public MatrixProd(int[][] A, int[][] B) {
        this.A = A;
        this.B = B;
        this.numRowsA = A.length;
        this.numColsA = A[0].length;
        this.numRowsB = B.length;
        this.numColsB = B[0].length;
        if (numColsA != numRowsB)
            throw new Error(String.format("Invalid dimensions; Cannot multiply %dx%d*%dx%d\n", numRowsA, numRowsB, numColsA, numColsB));
    }

    public int[][] seqProduct() {
        assert numColsA == numRowsB;
        int[][] C = new int[numRowsA][numColsB];
        for (int i = 0; i < numRowsA; i++) {
            for (int j = 0; j < numColsB; j++) {
                int sum = 0;
                for (int k = 0; k < numColsA; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        }
        return C;
    }

    public int[][] parProduct() {
        int numWorkers = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(numWorkers);
        int chunkSize = (int) Math.ceil((double) numRowsA / numWorkers);
        Future<int[][]>[] futures = new Future[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            int start = Math.min(i * chunkSize, numRowsA);
            int end = Math.min((i + 1) * chunkSize, numRowsA);
            futures[i] = pool.submit(new ParallelWorker(start, end));
        }
        int[][] C = new int[numRowsA][numColsB];
        try {
            for (int i = 0; i < numWorkers; i++) {
                int[][] partialC = futures[i].get();
                for (int j = 0; j < partialC.length; j++) {
                    for (int k = 0; k < numColsB; k++) {
                        C[j + (i * chunkSize)][k] = partialC[j][k];
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        return C;
    }

    private class ParallelWorker implements Callable<int[][]> {

        private int rowStart, rowEnd;

        public ParallelWorker(int rowStart, int rowEnd) {
            this.rowStart = rowStart;
            this.rowEnd = rowEnd;
        }

        @Override
        public int[][] call() throws Exception {
            int[][] C = new int[rowEnd-rowStart][numColsB];
            for (int i = 0; i < rowEnd-rowStart; i++) {
                for (int j = 0; j < numColsB; j++) {
                    int sum = 0;
                    for (int k = 0; k < numColsA; k++) {
                        sum += A[i+rowStart][k] * B[k][j];
                    }
                    C[i][j] = sum;
                }
            }
            return C;
        }
    }

    public static int[][] generateRandomMatrix(int M, int N) {
        System.out.format("Generating random %d x %d matrix...\n", M, N);
        Random rand = new Random();
        int[][] output = new int[M][N];
        for (int i=0; i<M; i++)
            for (int j=0; j<N; j++)
                output[i][j] = rand.nextInt(100);
        return output;
    }

    public static void main(String[] argv) {
        final int NUM_EVAL_RUNS = 5;
        final int[][] A = generateRandomMatrix(2000,2000);
        final int[][] B = generateRandomMatrix(2000,2000);

        MatrixProd mp = new MatrixProd(A,B);
        long execTime = 0;
        long start = System.currentTimeMillis();
        int[][] result = mp.seqProduct();
        execTime = System.currentTimeMillis() - start;
        System.out.format("Time elapsed for sequential computation: %d\n", execTime);
        start = System.currentTimeMillis();
        result = mp.parProduct();
        execTime = System.currentTimeMillis() - start;
        System.out.format("Time elapsed for parallel computation: %d\n", execTime);
    }
}
