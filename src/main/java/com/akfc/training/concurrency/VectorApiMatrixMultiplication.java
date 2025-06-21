package com.akfc.training.concurrency;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;
import jdk.incubator.vector.VectorOperators;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Demonstrates the use of Java's Vector API for matrix multiplication.
 * 
 * The Vector API (introduced as an incubator feature in Java 16) provides
 * a way to express vector computations that can be compiled to optimal
 * vector hardware instructions on supported platforms.
 * 
 * This example compares:
 * 1. Traditional scalar matrix multiplication
 * 2. Vectorized matrix multiplication using Vector API
 * 3. Parallel vectorized matrix multiplication using ForkJoinPool
 */
public class VectorApiMatrixMultiplication {
    
    // Vector species defines the shape and element type of vectors
    private static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;
    
    private final float[][] matrixA;
    private final float[][] matrixB;
    private final int size;
    
    public VectorApiMatrixMultiplication(int size) {
        this.size = size;
        this.matrixA = generateRandomMatrix(size);
        this.matrixB = generateRandomMatrix(size);
    }
    
    /**
     * Traditional scalar matrix multiplication - O(n³) complexity
     * Each element C[i][j] = sum(A[i][k] * B[k][j]) for k = 0 to n-1
     */
    public float[][] traditionalMultiply() {
        float[][] result = new float[size][size];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                float sum = 0.0f;
                for (int k = 0; k < size; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                result[i][j] = sum;
            }
        }
        
        return result;
    }
    
    /**
     * Vectorized matrix multiplication using Vector API
     * 
     * The Vector API allows us to process multiple elements simultaneously
     * using SIMD (Single Instruction, Multiple Data) operations.
     * 
     * Key concepts demonstrated:
     * - VectorSpecies: defines the shape and type of vectors
     * - fromArray(): loads data from arrays into vectors
     * - mul(): vectorized multiplication
     * - add(): vectorized addition
     * - reduceLanes(): reduces vector lanes to a single value
     */
    public float[][] vectorizedMultiply() {
        float[][] result = new float[size][size];
        int vectorLength = SPECIES.length();
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                float sum = 0.0f;
                int k = 0;
                
                // Process elements in chunks of vector length
                for (; k <= size - vectorLength; k += vectorLength) {
                    // Load vector chunks from row i of matrix A and column j of matrix B
                    FloatVector vecA = FloatVector.fromArray(SPECIES, matrixA[i], k);
                    
                    // For matrix B, we need to extract column j values
                    float[] columnB = new float[vectorLength];
                    for (int idx = 0; idx < vectorLength; idx++) {
                        columnB[idx] = matrixB[k + idx][j];
                    }
                    FloatVector vecB = FloatVector.fromArray(SPECIES, columnB, 0);
                    
                    // Vectorized multiplication and accumulation
                    FloatVector product = vecA.mul(vecB);
                    sum += product.reduceLanes(VectorOperators.ADD);
                }
                
                // Handle remaining elements (scalar cleanup)
                for (; k < size; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                
                result[i][j] = sum;
            }
        }
        
        return result;
    }
    
    /**
     * Parallel vectorized matrix multiplication using ForkJoinPool
     * 
     * Combines the benefits of:
     * - Vector API for SIMD operations
     * - Fork-Join framework for parallel processing
     */
    public float[][] parallelVectorizedMultiply() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        try {
            return pool.invoke(new MatrixMultiplyTask(0, size));
        } finally {
            // Note: We don't shutdown the common pool
        }
    }
    
    /**
     * RecursiveTask for parallel matrix multiplication
     * Uses divide-and-conquer approach to split work across threads
     */
    private class MatrixMultiplyTask extends RecursiveTask<float[][]> {
        private static final int THRESHOLD = 64; // Minimum size for parallel processing
        private final int startRow;
        private final int endRow;
        
        public MatrixMultiplyTask(int startRow, int endRow) {
            this.startRow = startRow;
            this.endRow = endRow;
        }
        
        @Override
        protected float[][] compute() {
            int rowCount = endRow - startRow;
            
            if (rowCount <= THRESHOLD) {
                // Base case: compute directly using vectorized approach
                return computeDirectly();
            } else {
                // Recursive case: split the work
                int midRow = startRow + rowCount / 2;
                
                MatrixMultiplyTask upperTask = new MatrixMultiplyTask(startRow, midRow);
                MatrixMultiplyTask lowerTask = new MatrixMultiplyTask(midRow, endRow);
                
                // Fork the upper task and compute lower task in current thread
                upperTask.fork();
                float[][] lowerResult = lowerTask.compute();
                float[][] upperResult = upperTask.join();
                
                // Combine results
                return combineResults(upperResult, lowerResult);
            }
        }
        
        private float[][] computeDirectly() {
            float[][] result = new float[endRow - startRow][size];
            int vectorLength = SPECIES.length();
            
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < size; j++) {
                    float sum = 0.0f;
                    int k = 0;
                    
                    // Vectorized computation
                    for (; k <= size - vectorLength; k += vectorLength) {
                        FloatVector vecA = FloatVector.fromArray(SPECIES, matrixA[i], k);
                        
                        float[] columnB = new float[vectorLength];
                        for (int idx = 0; idx < vectorLength; idx++) {
                            columnB[idx] = matrixB[k + idx][j];
                        }
                        FloatVector vecB = FloatVector.fromArray(SPECIES, columnB, 0);
                        
                        FloatVector product = vecA.mul(vecB);
                        sum += product.reduceLanes(VectorOperators.ADD);
                    }
                    
                    // Scalar cleanup
                    for (; k < size; k++) {
                        sum += matrixA[i][k] * matrixB[k][j];
                    }
                    
                    result[i - startRow][j] = sum;
                }
            }
            
            return result;
        }
        
        private float[][] combineResults(float[][] upper, float[][] lower) {
            float[][] combined = new float[upper.length + lower.length][size];
            
            // Copy upper part
            System.arraycopy(upper, 0, combined, 0, upper.length);
            
            // Copy lower part
            System.arraycopy(lower, 0, combined, upper.length, lower.length);
            
            return combined;
        }
    }
    
    /**
     * Advanced vectorized multiplication with better memory access patterns
     * 
     * This version demonstrates:
     * - Cache-friendly access patterns
     * - Block-wise computation to improve locality
     * - More efficient vector operations
     */
    public float[][] optimizedVectorizedMultiply() {
        float[][] result = new float[size][size];
        int vectorLength = SPECIES.length();
        int blockSize = 64; // Cache-friendly block size
        
        // Block-wise computation for better cache locality
        for (int ii = 0; ii < size; ii += blockSize) {
            for (int jj = 0; jj < size; jj += blockSize) {
                for (int kk = 0; kk < size; kk += blockSize) {
                    
                    // Process blocks
                    int iMax = Math.min(ii + blockSize, size);
                    int jMax = Math.min(jj + blockSize, size);
                    int kMax = Math.min(kk + blockSize, size);
                    
                    for (int i = ii; i < iMax; i++) {
                        for (int j = jj; j < jMax; j++) {
                            float sum = result[i][j]; // Accumulate existing value
                            int k = kk;
                            
                            // Vectorized inner loop
                            for (; k <= kMax - vectorLength; k += vectorLength) {
                                FloatVector vecA = FloatVector.fromArray(SPECIES, matrixA[i], k);
                                
                                // Pre-load column data for better performance
                                float[] columnB = new float[vectorLength];
                                for (int idx = 0; idx < vectorLength && k + idx < kMax; idx++) {
                                    columnB[idx] = matrixB[k + idx][j];
                                }
                                FloatVector vecB = FloatVector.fromArray(SPECIES, columnB, 0);
                                
                                sum += vecA.mul(vecB).reduceLanes(VectorOperators.ADD);
                            }
                            
                            // Handle remaining elements
                            for (; k < kMax; k++) {
                                sum += matrixA[i][k] * matrixB[k][j];
                            }
                            
                            result[i][j] = sum;
                        }
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * Generates a random matrix filled with values between 0 and 1
     */
    private static float[][] generateRandomMatrix(int size) {
        Random random = new Random(42); // Fixed seed for reproducible results
        float[][] matrix = new float[size][size];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextFloat();
            }
        }
        
        return matrix;
    }
    
    /**
     * Verifies that two matrices are approximately equal (within floating-point tolerance)
     */
    private static boolean matricesEqual(float[][] a, float[][] b, float tolerance) {
        if (a.length != b.length || a[0].length != b[0].length) {
            return false;
        }
        
        int errorCount = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (Math.abs(a[i][j] - b[i][j]) > tolerance) {
                    if (errorCount < 3) { // Only show first few errors
                        System.out.printf("Difference at [%d][%d]: %f vs %f (diff: %f)%n", 
                                        i, j, a[i][j], b[i][j], Math.abs(a[i][j] - b[i][j]));
                    }
                    errorCount++;
                }
            }
        }
        
        if (errorCount > 0) {
            System.out.printf("Total differences: %d (tolerance: %f)%n", errorCount, tolerance);
        }
        
        return errorCount == 0;
    }
    
    /**
     * Benchmarks different matrix multiplication approaches
     */
    private void benchmark() {
        System.out.println("=== Matrix Multiplication Benchmark ===");
        System.out.printf("Matrix size: %d x %d%n", size, size);
        System.out.printf("Vector species: %s (length: %d)%n", 
                         SPECIES, SPECIES.length());
        System.out.println();
        
        // Warm up JVM
        System.out.println("Warming up JVM...");
        for (int i = 0; i < 3; i++) {
            traditionalMultiply();
            vectorizedMultiply();
        }
        
        // Benchmark traditional approach
        long startTime = System.nanoTime();
        float[][] traditionalResult = traditionalMultiply();
        long traditionalTime = System.nanoTime() - startTime;
        
        // Benchmark vectorized approach
        startTime = System.nanoTime();
        float[][] vectorizedResult = vectorizedMultiply();
        long vectorizedTime = System.nanoTime() - startTime;
        
        // Benchmark parallel vectorized approach
        startTime = System.nanoTime();
        float[][] parallelResult = parallelVectorizedMultiply();
        long parallelTime = System.nanoTime() - startTime;
        
        // Benchmark optimized vectorized approach
        startTime = System.nanoTime();
        float[][] optimizedResult = optimizedVectorizedMultiply();
        long optimizedTime = System.nanoTime() - startTime;
        
        // Verify correctness (use more relaxed tolerance for floating-point comparison)
        boolean vectorizedCorrect = matricesEqual(traditionalResult, vectorizedResult, 1e-3f);
        boolean parallelCorrect = matricesEqual(traditionalResult, parallelResult, 1e-3f);
        boolean optimizedCorrect = matricesEqual(traditionalResult, optimizedResult, 1e-3f);
        
        // Print results
        System.out.println("=== Results ===");
        System.out.printf("Traditional:           %8.2f ms (baseline)%n", 
                         traditionalTime / 1_000_000.0);
        System.out.printf("Vectorized:            %8.2f ms (%.2fx %s)%n", 
                         vectorizedTime / 1_000_000.0,
                         (double) traditionalTime / vectorizedTime,
                         vectorizedCorrect ? "✓" : "✗");
        System.out.printf("Parallel Vectorized:   %8.2f ms (%.2fx %s)%n", 
                         parallelTime / 1_000_000.0,
                         (double) traditionalTime / parallelTime,
                         parallelCorrect ? "✓" : "✗");
        System.out.printf("Optimized Vectorized:  %8.2f ms (%.2fx %s)%n", 
                         optimizedTime / 1_000_000.0,
                         (double) traditionalTime / optimizedTime,
                         optimizedCorrect ? "✓" : "✗");
        
        System.out.println();
        System.out.println("=== Vector API Benefits ===");
        System.out.println("• SIMD operations: Process multiple elements simultaneously");
        System.out.println("• Hardware acceleration: Utilizes CPU vector instructions (AVX, NEON, etc.)");
        System.out.println("• Type safety: Compile-time vector type checking");
        System.out.println("• Performance: Can provide significant speedups for numerical computations");
        System.out.println("• Portability: Abstracts platform-specific vector instructions");
    }
    
    public static void main(String[] args) {
        // Test with different matrix sizes
        int[] sizes = {128, 256, 512};
        
        for (int size : sizes) {
            System.out.println("Testing with matrix size: " + size);
            VectorApiMatrixMultiplication demo = new VectorApiMatrixMultiplication(size);
            demo.benchmark();
            System.out.println();
        }
        
        // Demonstrate Vector API concepts
        demonstrateVectorApiConcepts();
    }
    
    /**
     * Demonstrates basic Vector API concepts with simple examples
     */
    private static void demonstrateVectorApiConcepts() {
        System.out.println("=== Vector API Concepts Demo ===");
        
        // 1. Vector creation and basic operations
        int vectorLength = SPECIES.length();
        float[] array1 = new float[vectorLength];
        float[] array2 = new float[vectorLength];
        
        // Fill arrays with sample data
        for (int i = 0; i < vectorLength; i++) {
            array1[i] = i + 1.0f;
            array2[i] = i + 2.0f;
        }
        
        System.out.println("1. Basic Vector Operations:");
        System.out.println("Vector length: " + vectorLength);
        System.out.println("Array 1: " + java.util.Arrays.toString(array1));
        System.out.println("Array 2: " + java.util.Arrays.toString(array2));
        
        // Load vectors from arrays
        FloatVector vec1 = FloatVector.fromArray(SPECIES, array1, 0);
        FloatVector vec2 = FloatVector.fromArray(SPECIES, array2, 0);
        
        // Perform vectorized operations
        FloatVector sum = vec1.add(vec2);
        FloatVector product = vec1.mul(vec2);
        FloatVector difference = vec2.sub(vec1);
        
        // Extract results back to arrays
        float[] sumResult = new float[vectorLength];
        float[] productResult = new float[vectorLength];
        float[] diffResult = new float[vectorLength];
        
        sum.intoArray(sumResult, 0);
        product.intoArray(productResult, 0);
        difference.intoArray(diffResult, 0);
        
        System.out.println("Sum:        " + java.util.Arrays.toString(sumResult));
        System.out.println("Product:    " + java.util.Arrays.toString(productResult));
        System.out.println("Difference: " + java.util.Arrays.toString(diffResult));
        
        // 2. Reduction operations
        System.out.println("\n2. Reduction Operations:");
        float totalSum = vec1.reduceLanes(VectorOperators.ADD);
        float maxValue = vec1.reduceLanes(VectorOperators.MAX);
        float minValue = vec1.reduceLanes(VectorOperators.MIN);
        
        System.out.printf("Total sum: %.2f%n", totalSum);
        System.out.printf("Max value: %.2f%n", maxValue);
        System.out.printf("Min value: %.2f%n", minValue);
        
        // 3. Masked operations
        System.out.println("\n3. Masked Operations:");
        var mask = vec1.compare(VectorOperators.GT, 4.0f);
        FloatVector maskedResult = vec1.mul(2.0f, mask);
        
        float[] maskedArray = new float[vectorLength];
        maskedResult.intoArray(maskedArray, 0);
        
        System.out.println("Original:      " + java.util.Arrays.toString(array1));
        System.out.println("Mask (>4.0):   " + mask);
        System.out.println("Doubled (>4):  " + java.util.Arrays.toString(maskedArray));
        
        System.out.println("\n=== Key Vector API Features ===");
        System.out.println("• Species: Defines vector shape and element type");
        System.out.println("• Lanes: Individual elements within a vector");
        System.out.println("• SIMD: Single Instruction, Multiple Data operations");
        System.out.println("• Masks: Conditional operations on vector elements");
        System.out.println("• Reductions: Combine all lanes into a single value");
        System.out.println("• Memory operations: Efficient loading/storing from/to arrays");
    }
}