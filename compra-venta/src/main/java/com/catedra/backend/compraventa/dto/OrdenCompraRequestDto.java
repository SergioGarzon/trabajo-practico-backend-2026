package com.catedra.backend.compraventa.dto;


import lombok.Data;


import lombok.NoArgsConstructor;



@NoArgsConstructor
public class OrdenCompraRequestDto {


    private String simboloAccion;

    private Double precioPorAccion;

    private Long cantidadPedida;
}
