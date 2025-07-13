package com.example.inventory_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
}
