package com.utnfrc.usuario_portfolios.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccionExternaDTO {
    @JsonProperty("name")
    private String nombre;
    @JsonProperty("symbol")
    private String simbolo;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }
}
