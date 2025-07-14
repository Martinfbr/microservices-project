package com.example.product.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Objeto de respuesta del producto")
public class ProductResponse {

    @Schema(description = "ID del producto", example = "1")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Smartphone Samsung")
    private String nombre;

    @Schema(description = "Precio del producto", example = "999.99", type = "number", format = "double")
    private BigDecimal precio;
}
