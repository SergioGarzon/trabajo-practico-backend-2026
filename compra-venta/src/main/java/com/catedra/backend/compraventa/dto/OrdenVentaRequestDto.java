package com.catedra.backend.compraventa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrdenVentaRequestDto {

    private Long usuarioId;
    private String simboloAccion;
    private Double precioPorAccion;
    private Long cantidadOriginal;
}
