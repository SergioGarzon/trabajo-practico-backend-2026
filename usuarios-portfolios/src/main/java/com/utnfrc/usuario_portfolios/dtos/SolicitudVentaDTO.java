package com.utnfrc.usuario_portfolios.dtos;

public class SolicitudVentaDTO {
    private Long idOrdenVenta;
    private String simboloAccion; // Ej: "AAPL"
    private Long cantidadAVender;
    private String mensaje;
    private Long cantidadInicial;
    private Long cantidadRestante;

    public SolicitudVentaDTO() {}

    public String getSimboloAccion() { return simboloAccion; }
    public void setSimboloAccion(String simboloAccion) { this.simboloAccion = simboloAccion; }
    public Long getCantidadAVender() { return cantidadAVender; }
    public void setCantidadAVender(Long cantidadAVender) { this.cantidadAVender = cantidadAVender; }
    public Long getIdOrdenVenta() { return idOrdenVenta; }
    public void setIdOrdenVenta(Long idOrdenVenta) { this.idOrdenVenta = idOrdenVenta; }
    public Long getCantidadInicial() { return cantidadInicial; }
    public void setCantidadInicial(Long cantidadInicial) {this.cantidadInicial = cantidadInicial;}
    public Long getCantidadRestante() { return cantidadRestante; }
    public void setCantidadRestante(Long cantidadRestante) { this.cantidadRestante = cantidadRestante; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}