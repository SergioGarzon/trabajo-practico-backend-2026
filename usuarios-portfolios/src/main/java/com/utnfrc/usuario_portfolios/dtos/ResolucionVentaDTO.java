package com.utnfrc.usuario_portfolios.dtos;

public class ResolucionVentaDTO {
    private String idOrdenVenta; // El UUID que generamos nosotros
    private Long cantidadVendida; // Cuántas logró vender en esta pasada
    private Long dineroObtenido; // Cuánta plata real en ARS le sumamos a la billetera

    public String getIdOrdenVenta() { return idOrdenVenta; }
    public void setIdOrdenVenta(String idOrdenVenta) { this.idOrdenVenta = idOrdenVenta; }
    public Long getCantidadVendida() { return cantidadVendida; }
    public void setCantidadVendida(Long cantidadVendida) { this.cantidadVendida = cantidadVendida; }
    public Long getDineroObtenido() { return dineroObtenido; }
    public void setDineroObtenido(Long dineroObtenido) { this.dineroObtenido = dineroObtenido; }
}