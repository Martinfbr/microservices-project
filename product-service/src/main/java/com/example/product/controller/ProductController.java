package com.example.product.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.product.dto.ProductRequest;
import com.example.product.dto.ProductResponse;
import com.example.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        logger.info("Solicitud para crear producto: {}", request);
        ProductResponse response = productService.create(request);
        logger.info("Producto creado con éxito: {}", response);
        return response;
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
        logger.info("Buscando producto con ID: {}", id);
        ProductResponse response = productService.getById(id);
        logger.info("Producto encontrado: {}", response);
        return response;
    }

    @Operation(summary = "Listar todos los productos con paginación")
    @ApiResponse(responseCode = "200", description = "Lista paginada de productos")
    @GetMapping
    public Page<ProductResponse> getAll(
            @Parameter(description = "Parámetros de paginación")
            Pageable pageable) {
        logger.info("Listando productos con paginación: {}", pageable);
        Page<ProductResponse> result = productService.getAll(pageable);
        logger.info("Productos encontrados: totalElements={}, totalPages={}",
                result.getTotalElements(), result.getTotalPages());
        return result;
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
        logger.info("Solicitud para actualizar producto con ID {}: {}", id, request);
        ProductResponse response = productService.update(id, request);
        logger.info("Producto actualizado con éxito: {}", response);
        return response;
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
        logger.info("Solicitud para eliminar producto con ID: {}", id);
        productService.delete(id);
        logger.info("Producto eliminado con éxito, ID: {}", id);
    }
}
