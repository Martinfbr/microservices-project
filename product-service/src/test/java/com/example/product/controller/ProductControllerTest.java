package com.example.product.controller;

import com.example.product.dto.ProductRequest;
import com.example.product.dto.ProductResponse;
import com.example.product.exception.ResourceNotFoundException;
import com.example.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductResponse sampleResponse;

    @BeforeEach
    void setup() {
        sampleResponse = ProductResponse.builder()
                .id(1L)
                .nombre("Test Product")
                .precio(new BigDecimal("1500.00"))
                .build();
    }

    /**
     * Test que verifica que se cree un producto exitosamente desde el controlador.
     */
    @Test
    void testCreate_shouldReturnCreatedProduct() {
        ProductRequest request = new ProductRequest();
        request.setNombre("Test Product");
        request.setPrecio(new BigDecimal("1500.00"));

        when(productService.create(request)).thenReturn(sampleResponse);

        ProductResponse result = productController.create(request);

        assertNotNull(result);
        assertEquals("Test Product", result.getNombre());
        assertThat(result.getPrecio()).isEqualByComparingTo("1500.00");
    }

    /**
     * Test que verifica que se obtenga un producto por ID correctamente desde el controlador.
     */
    @Test
    void testGetById_shouldReturnProduct() {
        when(productService.getById(1L)).thenReturn(sampleResponse);

        ProductResponse result = productController.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getNombre());
    }

    /**
     * Test que verifica que se devuelva una lista paginada de productos desde el controlador.
     */
    @Test
    void testGetAll_shouldReturnPagedProducts() {
        Page<ProductResponse> pagedResponse = new PageImpl<>(List.of(sampleResponse));
        when(productService.getAll(PageRequest.of(0, 10))).thenReturn(pagedResponse);

        Page<ProductResponse> result = productController.getAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getNombre());
    }

    /**
     * Test que verifica que se actualice un producto exitosamente desde el controlador.
     */
    @Test
    void testUpdate_shouldReturnUpdatedProduct() {
        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setNombre("Updated Product");
        updateRequest.setPrecio(new BigDecimal("1800.00"));

        ProductResponse updatedResponse = ProductResponse.builder()
                .id(1L)
                .nombre("Updated Product")
                .precio(new BigDecimal("1800.00"))
                .build();

        when(productService.update(1L, updateRequest)).thenReturn(updatedResponse);

        ProductResponse result = productController.update(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Updated Product", result.getNombre());
        assertThat(result.getPrecio()).isEqualByComparingTo("1800.00");
    }

    /**
     * Test que verifica que se elimine un producto correctamente desde el controlador.
     */
    @Test
    void testDelete_shouldCallService() {
        doNothing().when(productService).delete(1L);

        productController.delete(1L);

        verify(productService, times(1)).delete(1L);
    }
    /**
     * Test que verifica que se lance una excepción cuando se intenta obtener un producto inexistente.
     */
    @Test
    void testGetById_shouldThrowNotFound_whenProductMissing() {
        Long missingId = 99L;
        when(productService.getById(missingId)).thenThrow(new ResourceNotFoundException("Producto no encontrado"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productController.getById(missingId);
        });

        assertEquals("Producto no encontrado", exception.getMessage());
    }

    /**
     * Test que verifica que se lance una excepción cuando se intenta actualizar un producto que no existe.
     */
    @Test
    void testUpdate_shouldThrowNotFound_whenProductMissing() {
        Long missingId = 42L;
        ProductRequest request = new ProductRequest();
        request.setNombre("Nombre X");
        request.setPrecio(new BigDecimal("999.99"));

        when(productService.update(eq(missingId), any(ProductRequest.class)))
                .thenThrow(new ResourceNotFoundException("No existe producto"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productController.update(missingId, request);
        });

        assertThat(exception.getMessage()).isEqualTo("No existe producto");
    }

    /**
     * Test que verifica que se lance una excepción cuando se intenta eliminar un producto inexistente.
     */
    @Test
    void testDelete_shouldThrowNotFound_whenProductMissing() {
        Long id = 77L;
        doThrow(new ResourceNotFoundException("Producto no encontrado")).when(productService).delete(id);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productController.delete(id);
        });

        assertEquals("Producto no encontrado", exception.getMessage());
    }
    /**
     * Test que verifica que se lance IllegalArgumentException si el request es nulo al crear.
     */
    @Test
    void testCreate_shouldThrowException_whenRequestIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.create(null);
        });

        assertEquals("La solicitud del producto no puede estar vacía", exception.getMessage());
    }

    /**
     * Test que verifica que se lance IllegalArgumentException cuando el ID es nulo en getById.
     */
    @Test
    void testGetById_shouldThrowException_whenIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.getById(null);
        });

        assertEquals("El ID debe ser mayor que cero", exception.getMessage());
    }

    /**
     * Test que verifica que se lance IllegalArgumentException cuando el ID es <= 0 en getById.
     */
    @Test
    void testGetById_shouldThrowException_whenIdIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.getById(0L);
        });

        assertEquals("El ID debe ser mayor que cero", exception.getMessage());
    }

    /**
     * Test que verifica que se lance IllegalArgumentException si el ID es inválido al actualizar.
     */
    @Test
    void testUpdate_shouldThrowException_whenIdIsInvalid() {
        ProductRequest request = new ProductRequest();
        request.setNombre("PC");
        request.setPrecio(new BigDecimal("400.00"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.update(0L, request);
        });

        assertEquals("El ID debe ser mayor que cero", exception.getMessage());
    }

    /**
     * Test que verifica que se lance IllegalArgumentException si el request es nulo al actualizar.
     */
    @Test
    void testUpdate_shouldThrowException_whenRequestIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.update(1L, null);
        });

        assertEquals("La solicitud de actualización no puede estar vacía", exception.getMessage());
    }

    /**
     * Test que verifica que se lance IllegalArgumentException si el ID es inválido al eliminar.
     */
    @Test
    void testDelete_shouldThrowException_whenIdIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.delete(0L);
        });

        assertEquals("El ID debe ser mayor que cero", exception.getMessage());
    }


}
