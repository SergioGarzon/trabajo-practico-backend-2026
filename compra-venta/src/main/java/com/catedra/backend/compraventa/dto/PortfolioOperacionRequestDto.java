package com.catedra.backend.compraventa.dto;

import lombok.AllArgsConstructor;


import lombok.Data;


import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
public class PortfolioOperacionRequestDto {


    private String usuarioId;


    private String simboloAccion;


    private Long cantidad;
}
