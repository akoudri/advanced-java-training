package com.akfc.training.misc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ServiceTest {

    @Test
    public void testServiceMethod() {
        // Create a mock object of the repository
        Repository mockRepository = mock(Repository.class);

        // Configure the mock to return a specific value when a method is called
        when(mockRepository.getData()).thenReturn("Expected Result");

        // Create an instance of the service, injecting the mock repository
        Service service = new Service(mockRepository);

        // Call the method under test
        String result = service.performAction();

        // Verify the result
        assertEquals("Expected Result", result);

        // Verify interactions with the mock
        verify(mockRepository).getData();
    }
}
