package com.example.inventory_service.client;

import com.example.inventory_service.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://product-service:8081")
public interface ProductClient {

    @GetMapping("/api/v1/products/{id}")
    ProductoDto getProductoById(@PathVariable("id") Long id);  // Asegúrate de que Long esté bien escrito
}
