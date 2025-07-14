package com.example.product.service;

import com.example.product.dto.ProductRequest;
import com.example.product.dto.ProductResponse;
import com.example.product.exception.ResourceNotFoundException;
import com.example.product.mapper.ProductMapper;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test que verifica que se cree un producto correctamente.
     */
    @Test
    void create_shouldReturnProductResponse() {
        ProductRequest request = new ProductRequest();
        request.setNombre("Laptop");
        request.setPrecio(new BigDecimal("1500.00"));

        Product product = new Product(1L, "Laptop", new BigDecimal("1500.00"));
        ProductResponse response = new ProductResponse(1L, "Laptop", new BigDecimal("1500.00"));

        when(mapper.toEntity(request)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);
        when(mapper.toResponse(product)).thenReturn(response);

        ProductResponse result = service.create(request);

        assertNotNull(result);
        assertEquals("Laptop", result.getNombre());
    }

    /**
     * Test que verifica que se obtenga un producto por su ID.
     */
    @Test
    void getById_shouldReturnProductResponse() {
        Long id = 1L;
        Product product = new Product(id, "Tablet", new BigDecimal("299.00"));
        ProductResponse response = new ProductResponse(id, "Tablet", new BigDecimal("299.00"));

        when(repository.findById(id)).thenReturn(Optional.of(product));
        when(mapper.toResponse(product)).thenReturn(response);

        ProductResponse result = service.getById(id);

        assertEquals("Tablet", result.getNombre());
        assertEquals(new BigDecimal("299.00"), result.getPrecio());
    }

    /**
     * Test que verifica que se lance una excepción si no se encuentra el producto.
     */
    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(1L));
    }

    /**
     * Test que verifica que se devuelvan todos los productos paginados.
     */
    @Test
    void getAll_shouldReturnPageOfResponses() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product(1L, "Smartphone", new BigDecimal("899.00"));
        ProductResponse response = new ProductResponse(1L, "Smartphone", new BigDecimal("899.00"));

        Page<Product> page = new PageImpl<>(Collections.singletonList(product));
        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(product)).thenReturn(response);

        Page<ProductResponse> result = service.getAll(pageable);

        assertEquals(1, result.getTotalElements());
    }

    /**
     * Test que verifica que se actualice un producto correctamente.
     */
    @Test
    void update_shouldUpdateAndReturnResponse() {
        Long id = 1L;
        Product existing = new Product(id, "TV", new BigDecimal("500.00"));
        ProductRequest update = new ProductRequest();
        update.setNombre("Smart TV");
        update.setPrecio(new BigDecimal("700.00"));

        Product updatedProduct = new Product(id, "Smart TV", new BigDecimal("700.00"));
        ProductResponse response = new ProductResponse(id, "Smart TV", new BigDecimal("700.00"));

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(updatedProduct);
        when(mapper.toResponse(updatedProduct)).thenReturn(response);

        ProductResponse result = service.update(id, update);

        assertEquals("Smart TV", result.getNombre());
        assertEquals(new BigDecimal("700.00"), result.getPrecio());
    }

    /**
     * Test que verifica que se lance una excepción si no se encuentra el producto a actualizar.
     */
    @Test
    void update_shouldThrowException_whenNotFound() {
        ProductRequest update = new ProductRequest();
        update.setNombre("TV");
        update.setPrecio(new BigDecimal("300.00"));

        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, update));
    }

    /**
     * Test que verifica que se elimine un producto correctamente.
     */
    @Test
    void delete_shouldRemoveProduct() {
        Long id = 1L;
        Product product = new Product(id, "Router", new BigDecimal("100.00"));

        when(repository.findById(id)).thenReturn(Optional.of(product));

        service.delete(id);

        verify(repository, times(1)).delete(product);
    }

    /**
     * Test que verifica que se lance una excepción si no se encuentra el producto a eliminar.
     */
    @Test
    void delete_shouldThrowException_whenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(1L));
    }
    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el request de creación es nulo o inválido")
    void create_InvalidRequest_ThrowsException() {
        ProductRequest invalid = new ProductRequest();
        invalid.setNombre(null);
        invalid.setPrecio(null);

        assertThrows(IllegalArgumentException.class, () -> service.create(invalid));

        ProductRequest negativePrice = new ProductRequest();
        negativePrice.setNombre("Test");
        negativePrice.setPrecio(BigDecimal.valueOf(-10));

        assertThrows(IllegalArgumentException.class, () -> service.create(negativePrice));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el ID en getById es nulo o menor que cero")
    void getById_InvalidId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.getById(null));
        assertThrows(IllegalArgumentException.class, () -> service.getById(0L));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el producto no existe")
    void getById_NotFound_ThrowsException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(1L));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando pageable es nulo")
    void getAll_NullPageable_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.getAll(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando los datos de actualización son inválidos")
    void update_InvalidData_ThrowsException() {
        ProductRequest invalid = new ProductRequest();
        invalid.setNombre(null);
        invalid.setPrecio(BigDecimal.TEN);

        assertThrows(IllegalArgumentException.class, () -> service.update(1L, invalid));

        invalid.setNombre("Test");
        invalid.setPrecio(BigDecimal.valueOf(-5));
        assertThrows(IllegalArgumentException.class, () -> service.update(1L, invalid));

        assertThrows(IllegalArgumentException.class, () -> service.update(null, invalid));
        assertThrows(IllegalArgumentException.class, () -> service.update(0L, invalid));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando ID es inválido en delete")
    void delete_InvalidId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.delete(null));
        assertThrows(IllegalArgumentException.class, () -> service.delete(0L));
    }
}
