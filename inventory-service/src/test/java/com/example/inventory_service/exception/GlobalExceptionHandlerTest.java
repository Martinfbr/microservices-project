package com.example.inventory_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleRuntime_shouldReturnBadRequestWithMessage() {
        // Arrange
        String errorMessage = "Ocurrió un error";
        RuntimeException exception = new RuntimeException(errorMessage);

        // Act
        ResponseEntity<String> response = handler.handleRuntime(exception);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errorMessage, response.getBody());
    }
}
