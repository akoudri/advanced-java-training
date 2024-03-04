package com.akfc.training;

import com.akfc.training.misc.Array;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}