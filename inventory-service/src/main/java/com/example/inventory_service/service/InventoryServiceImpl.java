package com.example.inventory_service.service;

import com.example.inventory_service.client.ProductClient;
import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.dto.InventoryUpdateRequest;
import com.example.inventory_service.dto.ProductoDto;
import com.example.inventory_service.model.Inventory;
import com.example.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductClient productClient;

    @Override
    public InventoryResponse getByProductId(Long productoId) {
        Inventory inventory = inventoryRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en inventario"));

        ProductoDto producto = productClient.getProductoById(productoId);
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        return InventoryResponse.builder()
                .productoId(producto.getId())
                .productoNombre(producto.getNombre())
                .cantidad(inventory.getCantidad())
                .build();
    }
    @Override
    public InventoryResponse updateStock(Long productId, InventoryUpdateRequest request) {
        // Validaciones iniciales
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("El ID del producto no es válido");
        }

        if (request == null || request.getCantidad() == null || request.getCantidad() < 0) {
            throw new IllegalArgumentException("La cantidad proporcionada no es válida");
        }

        // Consultar el producto
        ProductoDto producto;
        try {
            producto = obtenerProducto(productId);
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar el producto: " + e.getMessage(), e);
        }

        if (producto == null) {
            throw new RuntimeException("Producto no encontrado en product-service");
        }

        // Buscar o crear inventario
        Inventory inventory = inventoryRepository.findByProductoId(productId)
                .orElseGet(() -> Inventory.builder()
                        .productoId(productId)
                        .cantidad(0)
                        .build());

        // Actualizar y guardar
        inventory.setCantidad(request.getCantidad());

        try {
            inventoryRepository.save(inventory);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el inventario: " + e.getMessage(), e);
        }

        log.info("Producto consultado desde product-service: {}", producto);
        log.info("Evento: Inventario actualizado para producto ID {}. Nueva cantidad: {}", productId, inventory.getCantidad());

        return InventoryResponse.builder()
                .productoId(productId)
                .productoNombre(producto.getNombre())
                .cantidad(inventory.getCantidad())
                .build();
    }



    private ProductoDto obtenerProducto(Long productId) {
        try {
            ProductoDto producto = productClient.getProductoById(productId);
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado en product-service");
            }
            return producto;
        } catch (Exception e) {
            log.error("Error al obtener el producto desde product-service: {}", e.getMessage());
            return null; // o lanzar excepción si prefieres
        }
    }

    @Override
    public List<InventoryResponse> getAllInventory() {
        List<Inventory> inventories = inventoryRepository.findAll();

        return inventories.stream().map(inventory -> {
            ProductoDto producto = obtenerProducto(inventory.getProductoId());

            return InventoryResponse.builder()
                    .productoId(inventory.getProductoId())
                    .productoNombre(producto.getNombre())
                    .cantidad(inventory.getCantidad())
                    .build();
        }).toList();
    }

}
