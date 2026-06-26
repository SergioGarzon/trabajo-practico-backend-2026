package com.catedra.backend.compraventa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResolucionVentaDTO {
    private String idOrdenVenta;
    private Long cantidadVendida;
    private Long dineroObtenido;
}
