package com.example.inventory_service.controller;

import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.dto.InventoryUpdateRequest;
import com.example.inventory_service.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@Tag(name = "Inventario", description = "Operaciones del microservicio de inventario")
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    private InventoryService inventoryService;

    @Operation(summary = "Consultar inventario por ID de producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado en inventario")
    })
    @GetMapping("/{productId}")
    public InventoryResponse getByProductId(
            @PathVariable
            @Parameter(description = "ID del producto") Long productId) {
        logger.info("Consultando inventario para producto ID: {}", productId);
        return inventoryService.getByProductId(productId);
    }

    @Operation(summary = "Actualizar stock de un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado en inventario")
    })
    @PostMapping("/{productId}")
    public InventoryResponse updateStock(
            @PathVariable Long productId,
            @Valid @RequestBody InventoryUpdateRequest request) {
        logger.info("Actualizando inventario para producto ID {}: {}", productId, request);
        return inventoryService.updateStock(productId, request);
    }
}
