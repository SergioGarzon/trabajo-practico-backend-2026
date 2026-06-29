package com.utnfrc.usuario_portfolios.dtos;

public class ResolucionVentaDTO {
    private Long idOrdenVenta; // El UUID que generamos nosotros
    private Long cantidadVendida; // Cuántas logró vender en esta pasada
    private Double dineroObtenido; // Cuánta plata real en ARS le sumamos a la billetera
    private String mensaje;

    public Long getIdOrdenVenta() { return idOrdenVenta; }
    public void setIdOrdenVenta(Long idOrdenVenta) { this.idOrdenVenta = idOrdenVenta; }
    public Long getCantidadVendida() { return cantidadVendida; }
    public void setCantidadVendida(Long cantidadVendida) { this.cantidadVendida = cantidadVendida; }
    public Double getDineroObtenido() { return dineroObtenido; }
    public void setDineroObtenido(Double dineroObtenido) { this.dineroObtenido = dineroObtenido; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}