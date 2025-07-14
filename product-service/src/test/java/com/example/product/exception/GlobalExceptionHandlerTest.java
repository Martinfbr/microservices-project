package com.example.product.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    /**
     * Test que verifica la respuesta cuando se lanza ResourceNotFoundException.
     */
    @Test
    void handleNotFound_shouldReturn404WithCorrectBody() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Producto no encontrado");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/products/99");

        ResponseEntity<Object> response = exceptionHandler.handleNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(404, body.get("status"));
        assertEquals("Producto no encontrado", body.get("message"));
        assertEquals("/api/v1/products/99", body.get("path"));
        assertEquals("Not Found", body.get("error"));
        assertNotNull(body.get("timestamp"));
    }

    /**
     * Test que verifica la respuesta para una excepción genérica.
     */
    @Test
    void handleGenericException_shouldReturn500WithGenericMessage() {
        Exception ex = new RuntimeException("Falla en base de datos");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/products");

        ResponseEntity<Object> response = exceptionHandler.handleGenericException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(500, body.get("status"));
        assertEquals("Ocurrió un error inesperado. Intente más tarde.", body.get("message"));
        assertEquals("/api/v1/products", body.get("path"));
        assertEquals("Internal Server Error", body.get("error"));
        assertNotNull(body.get("timestamp"));
    }
}
