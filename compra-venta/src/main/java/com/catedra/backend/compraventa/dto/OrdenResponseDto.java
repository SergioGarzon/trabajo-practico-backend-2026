package com.catedra.backend.compraventa.dto;

import com.catedra.backend.compraventa.entity.enums.EstadoOrden;
import lombok.Data;


import lombok.NoArgsConstructor;



import java.time.LocalDateTime;

@NoArgsConstructor
public class OrdenResponseDto {

    private Long id;
    private String usuarioId;
    private String simboloAccion;
    private Double precioPorAccion;
    private EstadoOrden estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaFinalizacion;
    private String tipoOrden;
    private Long cantidad;
}
