package com.utnfrc.usuario_portfolios.dtos;

public class SolicitudDineroDTO {
    private String idTransaccion;
    private Long monto;
    private String estadoAccion; // "CONFIRMAR" o "RECHAZAR" (Para la segunda fase)
    private String simbolo;
    private Long cantidad;

    // Getters y Setters
    public Long getMonto() { return monto; }
    public void setMonto(Long monto) { this.monto = monto; }
    public String getEstadoAccion() { return estadoAccion; }
    public void setEstadoAccion(String estadoAccion) { this.estadoAccion = estadoAccion; }
    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }
    public Long getCantidad() { return cantidad; }
    public void setCantidad(Long cantidad) { this.cantidad = cantidad; }
    public String getIdTransaccion() { return idTransaccion; }
    public void setIdTransaccion(String idTransaccion) { this.idTransaccion = idTransaccion; }
}