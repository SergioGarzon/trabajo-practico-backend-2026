package com.utnfrc.usuario_portfolios.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BilleteraOperacionRequestDto {

    @NotNull
    private Long usuarioId;

    @NotNull
    @Positive
    private Double monto;

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
}
