package com.akfc.training;

import com.akfc.training.misc.Array;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.Stream;

class ArrayTest {

    private Array array;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Setting up all tests");
    }

    @BeforeEach
    void setUp() {
        array = new Array(10, 100);
    }

    @Test
    void bubblesSort() {
        array.bubblesSort();
        int[] values = array.getValues();
        for (int i = 0; i < values.length - 1; i++)
            assert values[i] <= values[i + 1];
    }

    @Test
    void insertionSort() {
        array.insertionSort();
        int[] values = array.getValues();
        for (int i = 0; i < values.length - 1; i++)
            assert values[i] <= values[i + 1];
    }

    @Test
    void quickSort() {
        array.quickSort();
        int[] values = array.getValues();
        for (int i = 0; i < values.length - 1; i++)
            assert values[i] <= values[i + 1];
    }

    @TestFactory
    Stream<DynamicTest> testSortingAlgorithms() { //Tests dynamiques
        return Stream.of(
                // Tests pour bubbleSort
                DynamicTest.dynamicTest("Bubble Sort - Tableau aléatoire", () -> {
                    Array array = new Array(10, 100);
                    int[] originalValues = Arrays.copyOf(array.getValues(), array.getValues().length);

                    array.bubblesSort();

                    Arrays.sort(originalValues);
                    assertArrayEquals(originalValues, array.getValues());
                }),

                // Tests pour insertionSort
                DynamicTest.dynamicTest("Insertion Sort - Tableau déjà trié", () -> {
                    int[] sortedArray = {1, 2, 3, 4, 5};
                    Array array = new Array(sortedArray);

                    array.insertionSort();

                    assertArrayEquals(sortedArray, array.getValues());
                }),

                // Tests pour quickSort
                DynamicTest.dynamicTest("Quick Sort - Tableau inversé", () -> {
                    int[] reversedArray = {5, 4, 3, 2, 1};
                    Array array = new Array(reversedArray);

                    array.quickSort();

                    assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array.getValues());
                })
        );
    }
}