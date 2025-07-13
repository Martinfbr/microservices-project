package com.example.product.service;

import com.example.product.dto.ProductRequest;
import com.example.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    ProductResponse getById(Long id);
    Page<ProductResponse> getAll(Pageable pageable);
    ProductResponse update(Long id, ProductRequest request);
    void delete(Long id);
}
