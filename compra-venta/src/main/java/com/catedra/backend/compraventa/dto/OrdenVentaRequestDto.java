package com.catedra.backend.compraventa.dto;


import lombok.Data;


import lombok.NoArgsConstructor;



@NoArgsConstructor
public class OrdenVentaRequestDto {

    private String simboloAccion;


    private Double precioPorAccion;


    private Long cantidadOriginal;
}
