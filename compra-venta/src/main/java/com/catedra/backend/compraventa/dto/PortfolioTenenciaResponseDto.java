package com.catedra.backend.compraventa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PortfolioTenenciaResponseDto {

    private Long usuarioId;
    private String simboloAccion;
    private Long cantidadDisponible;
}
