package com.example.inventory_service.controller;

import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.dto.InventoryUpdateRequest;
import com.example.inventory_service.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private InventoryResponse sampleResponse;

    @BeforeEach
    void setup() {
        sampleResponse = InventoryResponse.builder()
                .productoId(1L)
                .productoNombre("Test Product")
                .cantidad(100)
                .build();
    }

    @Test
    void testGetAllInventory_shouldReturnList() {
        when(inventoryService.getAllInventory()).thenReturn(List.of(sampleResponse));

        List<InventoryResponse> result = inventoryController.getAllInventory();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getProductoNombre());
    }

    @Test
    void testGetByProductId_shouldReturnResponse() {
        when(inventoryService.getByProductId(1L)).thenReturn(sampleResponse);

        InventoryResponse result = inventoryController.getByProductId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getProductoId());
        assertEquals("Test Product", result.getProductoNombre());
    }

    @Test
    void testUpdateStock_shouldReturnUpdatedResponse() {
        InventoryUpdateRequest request = new InventoryUpdateRequest();
        request.setCantidad(200);

        when(inventoryService.updateStock(1L, request)).thenReturn(sampleResponse);

        InventoryResponse result = inventoryController.updateStock(1L, request);

        assertNotNull(result);
        assertEquals(1L, result.getProductoId());
        assertEquals("Test Product", result.getProductoNombre());
    }
}
