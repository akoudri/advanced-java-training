package com.akfc.training.concurrency;

import java.util.Random;

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
        int[][] C = new int[numRowsA][numColsB];
        //TODO: perform computation
        return C;
    }

    public int[][] parProduct() {
        int[][] C = new int[numRowsA][numColsB];
        //TODO: create thread pool
        // and merge partial results
        return C;
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
