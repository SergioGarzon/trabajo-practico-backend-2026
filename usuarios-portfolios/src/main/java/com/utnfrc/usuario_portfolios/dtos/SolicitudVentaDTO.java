package com.utnfrc.usuario_portfolios.dtos;

public class SolicitudVentaDTO {
    private String simboloAccion; // Ej: "AAPL"
    private Long cantidadAVender;

    public String getSimboloAccion() { return simboloAccion; }
    public void setSimboloAccion(String simboloAccion) { this.simboloAccion = simboloAccion; }
    public Long getCantidadAVender() { return cantidadAVender; }
    public void setCantidadAVender(Long cantidadAVender) { this.cantidadAVender = cantidadAVender; }
}