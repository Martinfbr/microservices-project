package com.example.inventory_service.service;

import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.dto.InventoryUpdateRequest;

public interface InventoryService {
    InventoryResponse getByProductId(Long productId);
    InventoryResponse updateStock(Long productId, InventoryUpdateRequest request);
}
