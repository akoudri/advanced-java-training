package com.akfc.training;

import com.akfc.training.misc.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Calculator Test")
public class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @ParameterizedTest
    @MethodSource("pgcdTestData")
    @DisplayName("Test PGCD")
    void testPGCD(int a, int b, int expected) {
        int pgcd = calculator.pgcd(a, b);
        assertEquals(expected, pgcd, () -> "PGCD de " + a + " et " + b + " doit être " + expected);
    }

    private static Stream<Arguments> pgcdTestData() {
        return Stream.of(
                Arguments.of(12, 18, 6),
                Arguments.of(25, 15, 5),
                Arguments.of(42, 36, 6)
        );
    }

    @Test
    void testSQrt() {
        double x = 25.0;
        double epsilon = 0.0001;
        double expected = 5.0;
        double result = calculator.sqrt(x, epsilon);
        assertEquals(expected, result, epsilon, "La racine carrée de " + x + " doit être " + expected);
    }

}
