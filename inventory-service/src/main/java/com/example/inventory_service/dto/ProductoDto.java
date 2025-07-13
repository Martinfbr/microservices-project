package com.example.inventory_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoDto {
    private Long id;
    private String nombre;
    private BigDecimal precio;
}
