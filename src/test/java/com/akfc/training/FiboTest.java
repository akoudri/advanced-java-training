package com.akfc.training;

import com.akfc.training.misc.Fibo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FiboTest {

    private Fibo fibo;

    @BeforeEach
    void setUp() {
        fibo = new Fibo();
    }

    @Test
    void getEvenNumbers() {
        List<Long> expectedNumbers = Arrays.asList(2L, 8L, 34L, 144L);
        List<Long> actualNumbers = fibo.getEvenNumbers(200);
        assertEquals(expectedNumbers, actualNumbers, "The list of even numbers does not match the expected list.");
    }

    @Test
    void getEvenNumbersPerfo() {
        assertTimeout(Duration.ofMillis(2), () -> fibo.getEvenNumbers(4000));
    }
}