package com.example.inventory_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUpdateRequest {

    @NotNull(message = "Cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;
}
