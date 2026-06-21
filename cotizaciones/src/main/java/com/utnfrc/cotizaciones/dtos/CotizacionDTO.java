package com.utnfrc.cotizaciones.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionDTO {

    private String simbolo;
    private Double precioOriginal;
    private String monedaOriginal;
    private Double precioARS;

}
