package com.example.inventory_service.service;

import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.dto.InventoryUpdateRequest;
import com.example.inventory_service.model.Inventory;
import com.example.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final RestTemplate restTemplate;

    @Value("${product-service.url}")
    private String productServiceUrl;

    @Override
    public InventoryResponse getByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductoId(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en inventario"));

        String productoNombre = obtenerNombreProducto(productId);

        return InventoryResponse.builder()
                .productoId(inventory.getProductoId())
                .productoNombre(productoNombre)
                .cantidad(inventory.getCantidad())
                .build();
    }

    @Override
    public InventoryResponse updateStock(Long productId, InventoryUpdateRequest request) {
        Inventory inventory = inventoryRepository.findByProductoId(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en inventario"));

        inventory.setCantidad(request.getCantidad());
        inventoryRepository.save(inventory);

        String productoNombre = obtenerNombreProducto(productId);

        return InventoryResponse.builder()
                .productoId(productId)
                .productoNombre(productoNombre)
                .cantidad(request.getCantidad())
                .build();
    }

    private String obtenerNombreProducto(Long productId) {
        try {
            ResponseEntity<ProductoDto> response = restTemplate.getForEntity(
                    productServiceUrl + "/" + productId, ProductoDto.class);
            return response.getBody() != null ? response.getBody().getNombre() : "Producto desconocido";
        } catch (Exception e) {
            log.error("Error al obtener el producto del servicio externo: {}", e.getMessage());
            return "Producto desconocido";
        }
    }

    private static class ProductoDto {
        private String nombre;
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }
}
