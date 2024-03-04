package com.akfc.training;

import com.akfc.training.misc.Repository;
import com.akfc.training.misc.Service;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ServiceTest {
    @Mock
    Repository mockRepository;

    Service service;

    @BeforeEach
    public void setUp() {
        // Create a mock object of the repository
        mockRepository = mock(Repository.class);
        // Configure the mock to return a specific value when a method is called
        when(mockRepository.getData()).thenReturn("Expected Result");
        when(mockRepository.square(3)).thenReturn(9);
        // Create an instance of the service, injecting the mock repository
        service = new Service(mockRepository);
    }

    @Test
    public void testPerformAction() {
        // Call the method under test
        String result = service.performAction();
        // Verify the result
        assertEquals("Expected Result", result);
        // Verify interactions with the mock
        verify(mockRepository).getData();
    }

    @Test
    public void testPerformComputation() {
        // Call the method under test
        int result = service.performComputation(3);
        // Verify the result
        assertEquals(19, result);
        // Verify interactions with the mock
        verify(mockRepository).square(3);
    }

    @RepeatedTest(10)
    void testSquareComputation(RepetitionInfo repetitionInfo) {
        int currentRepetition = repetitionInfo.getCurrentRepetition();
        int totalRepetitions = repetitionInfo.getTotalRepetitions();
        System.out.println("Executing repetition " + currentRepetition + " of " + totalRepetitions);
        Random random = new Random();
        int number = random.nextInt(100);
        int expectedSquare;
        if (number < 10) {
            expectedSquare = number * number + 1; // Intentional bug
        } else {
            expectedSquare = number * number;
        }
        when(mockRepository.square(number)).thenReturn(expectedSquare);
        int actualSquare = service.square(number);
        assertEquals(expectedSquare, actualSquare);
    }
}
