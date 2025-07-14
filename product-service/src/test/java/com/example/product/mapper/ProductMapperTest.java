package com.example.product.mapper;

import com.example.product.dto.ProductRequest;
import com.example.product.dto.ProductResponse;
import com.example.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductMapper();
    }

    /**
     * Test que verifica que se mapee correctamente un ProductRequest a Product (entidad).
     */
    @Test
    void toEntity_shouldMapRequestToEntity() {
        ProductRequest request = new ProductRequest();
        request.setNombre("Mouse Gamer");
        request.setPrecio(new BigDecimal("49.99"));

        Product product = mapper.toEntity(request);

        assertNotNull(product);
        assertEquals("Mouse Gamer", product.getNombre());
        assertEquals(new BigDecimal("49.99"), product.getPrecio());
    }

    /**
     * Test que verifica que se mapee correctamente un Product (entidad) a ProductResponse (DTO).
     */
    @Test
    void toResponse_shouldMapEntityToResponse() {
        Product product = Product.builder()
                .id(5L)
                .nombre("Teclado Mecánico")
                .precio(new BigDecimal("129.50"))
                .build();

        ProductResponse response = mapper.toResponse(product);

        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals("Teclado Mecánico", response.getNombre());
        assertEquals(new BigDecimal("129.50"), response.getPrecio());
    }
}
