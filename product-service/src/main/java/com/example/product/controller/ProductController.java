package com.example.product.controller;

import com.example.product.dto.ProductRequest;
import com.example.product.dto.ProductResponse;
import com.example.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Productos", description = "Operaciones del microservicio de productos")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Operation(summary = "Crear un nuevo producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ProductResponse create(
            @Valid @RequestBody
            @Parameter(description = "Datos del producto a crear")
            ProductRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            throw new IllegalArgumentException("La solicitud del producto no puede estar vacía");
        }
        logger.info("Solicitud para crear producto: {}", request);
        return productService.create(request);
    }

    @Operation(summary = "Obtener un producto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ProductResponse getById(
            @PathVariable
            @Parameter(description = "ID del producto a consultar")
            Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero");
        }
        logger.info("Buscando producto con ID: {}", id);
        return productService.getById(id);
    }

    @Operation(summary = "Listar todos los productos con paginación")
    @ApiResponse(responseCode = "200", description = "Lista paginada de productos")
    @GetMapping
    public Page<ProductResponse> getAll(
            @Parameter(description = "Parámetros de paginación")
            Pageable pageable) {
        logger.info("Listando productos con paginación: {}", pageable);
        return productService.getAll(pageable);
    }

    @Operation(summary = "Actualizar un producto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ProductResponse update(
            @PathVariable
            @Parameter(description = "ID del producto a actualizar")
            Long id,
            @Valid @RequestBody
            @Parameter(description = "Datos actualizados del producto")
            ProductRequest request) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero");
        }
        if (ObjectUtils.isEmpty(request)) {
            throw new IllegalArgumentException("La solicitud de actualización no puede estar vacía");
        }
        logger.info("Solicitud para actualizar producto con ID {}: {}", id, request);
        return productService.update(id, request);
    }

    @Operation(summary = "Eliminar un producto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable
            @Parameter(description = "ID del producto a eliminar")
            Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero");
        }
        logger.info("Solicitud para eliminar producto con ID: {}", id);
        productService.delete(id);
    }
}
