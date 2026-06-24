package com.utnfrc.usuario_portfolios.dtos;

public class PortfolioTenenciaResponseDto {

    private Long usuarioId;
    private String simboloAccion;
    private Long cantidadDisponible;

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getSimboloAccion() { return simboloAccion; }
    public void setSimboloAccion(String simboloAccion) { this.simboloAccion = simboloAccion; }
    public Long getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(Long cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }
}
