package com.catedra.backend.compraventa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TransaccionResponseDto {

    private Long id;
    private Long ordenCompraId;
    private Long ordenVentaId;
    private Long cantidadAcciones;
    private Double precioEjecucion;
    private Double montoTotal;
    private LocalDateTime fecha;
}
