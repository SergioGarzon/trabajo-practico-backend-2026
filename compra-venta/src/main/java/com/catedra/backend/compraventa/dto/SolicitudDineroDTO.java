package com.catedra.backend.compraventa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDineroDTO {
    private String idTransaccion;
    private Long monto;
    private String estadoAccion;
    private String simbolo;
    private Long cantidad;
}
