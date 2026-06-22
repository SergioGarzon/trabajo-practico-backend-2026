package com.utnfrc.usuario_portfolios.dtos;

public class SolicitudDineroDTO {
    private Long monto;
    private String estadoAccion; // "CONFIRMAR" o "RECHAZAR" (Para la segunda fase)

    // Getters y Setters
    public Long getMonto() { return monto; }
    public void setMonto(Long monto) { this.monto = monto; }
    public String getEstadoAccion() { return estadoAccion; }
    public void setEstadoAccion(String estadoAccion) { this.estadoAccion = estadoAccion; }
}