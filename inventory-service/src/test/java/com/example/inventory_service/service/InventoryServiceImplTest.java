package com.example.inventory_service.service;

import com.example.inventory_service.client.ProductClient;
import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.dto.InventoryUpdateRequest;
import com.example.inventory_service.dto.ProductoDto;
import com.example.inventory_service.model.Inventory;
import com.example.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @BeforeEach
    void setUp() {
        inventoryRepository = mock(InventoryRepository.class);
        productClient = mock(ProductClient.class);
        inventoryService = new InventoryServiceImpl(inventoryRepository, productClient);
    }

    @Test
    void testGetByProductId_whenInventoryExists_shouldReturnInventoryResponse() {
        Long productId = 1L;
        Inventory inventory = new Inventory(1L, productId, 20);
        ProductoDto producto = new ProductoDto(productId, "Test Product", 10.0);

        when(inventoryRepository.findByProductoId(productId)).thenReturn(Optional.of(inventory));
        when(productClient.getProductoById(productId)).thenReturn(producto);

        InventoryResponse response = inventoryService.getByProductId(productId);

        assertNotNull(response);
        assertEquals(20, response.getCantidad());
        assertEquals("Test Product", response.getProductoNombre());
    }

    @Test
    void testUpdateStock_whenInventoryExists_shouldUpdateStock() {
        Long productId = 1L;
        Inventory inventory = new Inventory(1L, productId, 10);
        ProductoDto producto = new ProductoDto(productId, "Product Updated", 9.99);
        InventoryUpdateRequest request = new InventoryUpdateRequest();
        request.setCantidad(50);

        when(inventoryRepository.findByProductoId(productId)).thenReturn(Optional.of(inventory));
        when(productClient.getProductoById(productId)).thenReturn(producto);

        InventoryResponse response = inventoryService.updateStock(productId, request);

        assertEquals(50, response.getCantidad());
        assertEquals("Product Updated", response.getProductoNombre());
    }

    @Test
    void testUpdateStock_whenInventoryDoesNotExist_shouldCreateNewStock() {
        Long productId = 99L;
        ProductoDto producto = new ProductoDto(productId, "Nuevo Producto", 25.0);
        InventoryUpdateRequest request = new InventoryUpdateRequest();
        request.setCantidad(100);

        when(inventoryRepository.findByProductoId(productId)).thenReturn(Optional.empty());
        when(productClient.getProductoById(productId)).thenReturn(producto);

        InventoryResponse response = inventoryService.updateStock(productId, request);

        assertEquals(100, response.getCantidad());
        assertEquals("Nuevo Producto", response.getProductoNombre());
    }

    @Test
    void testGetByProductId_whenInventoryDoesNotExist_shouldThrowException() {
        Long productId = 123L;
        when(inventoryRepository.findByProductoId(productId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            inventoryService.getByProductId(productId);
        });

        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }
    @Test
    void testUpdateStock_shouldCreateInventoryWhenNotExists() {
        Long productId = 1L;
        InventoryUpdateRequest request = new InventoryUpdateRequest();
        request.setCantidad(20);

        ProductoDto producto = new ProductoDto();
        producto.setId(productId);
        producto.setNombre("Producto 1");

        when(productClient.getProductoById(productId)).thenReturn(producto);
        when(inventoryRepository.findByProductoId(productId)).thenReturn(Optional.empty());

        Inventory savedInventory = Inventory.builder()
                .productoId(productId)
                .cantidad(request.getCantidad())
                .build();

        when(inventoryRepository.save(any())).thenReturn(savedInventory);

        InventoryResponse response = inventoryService.updateStock(productId, request);

        assertNotNull(response);
        assertEquals(productId, response.getProductoId());
        assertEquals("Producto 1", response.getProductoNombre());
        assertEquals(20, response.getCantidad());
    }
    @Test
    void testUpdateStock_shouldUpdateExistingInventory() {
        Long productId = 1L;
        InventoryUpdateRequest request = new InventoryUpdateRequest();
        request.setCantidad(30);

        ProductoDto producto = new ProductoDto();
        producto.setId(productId);
        producto.setNombre("Producto Existente");

        Inventory existingInventory = Inventory.builder()
                .id(1L)
                .productoId(productId)
                .cantidad(10)
                .build();

        when(productClient.getProductoById(productId)).thenReturn(producto);
        when(inventoryRepository.findByProductoId(productId)).thenReturn(Optional.of(existingInventory));

        Inventory updatedInventory = Inventory.builder()
                .id(1L)
                .productoId(productId)
                .cantidad(30)
                .build();

        when(inventoryRepository.save(any())).thenReturn(updatedInventory);

        InventoryResponse response = inventoryService.updateStock(productId, request);

        assertNotNull(response);
        assertEquals(30, response.getCantidad());
        assertEquals("Producto Existente", response.getProductoNombre());
    }
    @Test
    void testUpdateStock_whenProductNotFound_shouldThrowException() {
        Long productId = 5L;
        InventoryUpdateRequest request = new InventoryUpdateRequest();
        request.setCantidad(30);

        when(productClient.getProductoById(productId)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                inventoryService.updateStock(productId, request));

        assertEquals("Producto no encontrado en product-service", ex.getMessage());
    }
    @Test
    void testUpdateStock_shouldThrowExceptionIfProductoNotFound() {
        Long productId = 999L;
        InventoryUpdateRequest request = new InventoryUpdateRequest();
        request.setCantidad(10);

        when(productClient.getProductoById(productId)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                inventoryService.updateStock(productId, request)
        );

        assertEquals("Producto no encontrado en product-service", exception.getMessage());
    }
    @Test
    void testGetByProductId_shouldThrowException_whenProductoIsNull() {
        Long productId = 3L;
        Inventory inventory = new Inventory(3L, productId, 50);

        when(inventoryRepository.findByProductoId(productId)).thenReturn(Optional.of(inventory));
        when(productClient.getProductoById(productId)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> inventoryService.getByProductId(productId));

        assertEquals("Producto no encontrado", exception.getMessage());
    }
    @Test
    void testGetAllInventory_shouldReturnMappedResponse() {
        Inventory inventory = Inventory.builder()
                .id(1L)
                .productoId(1L)
                .cantidad(50)
                .build();

        ProductoDto producto = new ProductoDto();
        producto.setId(1L);
        producto.setNombre("Producto A");

        when(inventoryRepository.findAll()).thenReturn(List.of(inventory));
        when(productClient.getProductoById(1L)).thenReturn(producto);

        List<InventoryResponse> result = inventoryService.getAllInventory();

        assertEquals(1, result.size());
        assertEquals("Producto A", result.get(0).getProductoNombre());
        assertEquals(50, result.get(0).getCantidad());
    }
    @Test
    void testUpdateStock_shouldThrowExceptionWhenProductIdInvalid() {
        InventoryUpdateRequest request = new InventoryUpdateRequest();
        request.setCantidad(10);

        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.updateStock(null, request));
        assertEquals("El ID del producto no es válido", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.updateStock(-1L, request));
        assertEquals("El ID del producto no es válido", exception2.getMessage());
    }

    @Test
    void testUpdateStock_shouldThrowExceptionWhenRequestIsInvalid() {
        Long productId = 1L;

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.updateStock(productId, null));
        assertEquals("La cantidad proporcionada no es válida", ex1.getMessage());

        InventoryUpdateRequest reqWithNull = new InventoryUpdateRequest();
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.updateStock(productId, reqWithNull));
        assertEquals("La cantidad proporcionada no es válida", ex2.getMessage());

        InventoryUpdateRequest reqNegative = new InventoryUpdateRequest();
        reqNegative.setCantidad(-5);
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.updateStock(productId, reqNegative));
        assertEquals("La cantidad proporcionada no es válida", ex3.getMessage());
    }

}
