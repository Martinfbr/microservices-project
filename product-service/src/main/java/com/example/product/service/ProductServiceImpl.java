package com.example.product.service;

import com.example.product.dto.ProductRequest;
import com.example.product.dto.ProductResponse;
import com.example.product.exception.ResourceNotFoundException;
import com.example.product.mapper.ProductMapper;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper mapper;

    @Override
    public ProductResponse create(ProductRequest request) {
        try {
            Product product = mapper.toEntity(request);
            Product saved = repository.save(product);
            logger.info("Producto creado exitosamente: {}", saved);
            return mapper.toResponse(saved);
        } catch (Exception ex) {
            logger.error("Error al crear el producto: {}", request, ex);
            throw ex;
        }
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Producto no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Producto no encontrado");
                });
        logger.info("Producto recuperado: {}", product);
        return mapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> getAll(Pageable pageable) {
        Page<Product> page = repository.findAll(pageable);
        logger.info("Productos recuperados: total={}, páginas={}", page.getTotalElements(), page.getTotalPages());
        return page.map(mapper::toResponse);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest updated) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Producto no encontrado para actualizar: ID={}", id);
                    return new ResourceNotFoundException("Producto no encontrado");
                });

        existing.setNombre(updated.getNombre());
        existing.setPrecio(updated.getPrecio());

        Product saved = repository.save(existing);
        logger.info("Producto actualizado: {}", saved);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Producto no encontrado para eliminar: ID={}", id);
                    return new ResourceNotFoundException("Producto no encontrado");
                });

        repository.delete(existing);
        logger.info("Producto eliminado: ID={}", id);
    }
}
