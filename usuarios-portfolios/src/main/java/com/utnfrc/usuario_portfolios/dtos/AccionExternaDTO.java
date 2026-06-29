package com.utnfrc.usuario_portfolios.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccionExternaDTO {
    @JsonProperty("name")
    private String nombre;
    @JsonProperty("symbol")
    private String simbolo;
}
