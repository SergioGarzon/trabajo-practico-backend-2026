package com.utnfrc.usuario_portfolios.dtos;

public class SolicitudDineroDTO {
    private Long idOrdenCompra;
    private Double monto;
    private String estadoAccion; // "CONFIRMAR" o "RECHAZAR" (Para la segunda fase)
    private String simbolo;
    private Long cantidad;
    private String mensaje;

    // Getters y Setters
    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
    public String getEstadoAccion() { return estadoAccion; }
    public void setEstadoAccion(String estadoAccion) { this.estadoAccion = estadoAccion; }
    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }
    public Long getCantidad() { return cantidad; }
    public void setCantidad(Long cantidad) { this.cantidad = cantidad; }
    public Long getIdOrdenCompra() { return idOrdenCompra; }
    public void setIdOrdenCompra(Long idOrdenCompra) { this.idOrdenCompra = idOrdenCompra; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}