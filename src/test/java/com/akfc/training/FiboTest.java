package com.akfc.training;

import com.akfc.training.misc.Fibo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("fibonacciTestData")
    void testFibonacci(int input, long expectedResult) {
        assertEquals(expectedResult, fibo.slide(input),
                () -> "Fibonacci de " + input + " doit être " + expectedResult);
    }

    private static Stream<Arguments> fibonacciTestData() {
        return Stream.of(
                Arguments.of(1, 1),
                Arguments.of(2, 1),
                Arguments.of(3, 3),
                Arguments.of(4, 5),
                Arguments.of(5, 8),
                Arguments.of(6, 13),
                Arguments.of(7, 21)
        );
    }
}