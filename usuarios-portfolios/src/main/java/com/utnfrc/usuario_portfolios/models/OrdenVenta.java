package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ordenes_venta")
public class OrdenVenta {

    @Id
    private String id; // UUID de la transacción

    @ManyToOne
    @JoinColumn(name = "item_portfolio_id")
    private ItemPortfolio itemPortfolio;

    private Long cantidadInicial;
    private Long cantidadRestante; // Lo que falta vender
    private String estado; // "PENDIENTE", "PARCIAL", "COMPLETADA", "CANCELADA"

    public OrdenVenta() {
        this.id = UUID.randomUUID().toString();
        this.estado = "PENDIENTE";
    }

    // Getters y Setters...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public ItemPortfolio getItemPortfolio() { return itemPortfolio; }
    public void setItemPortfolio(ItemPortfolio itemPortfolio) { this.itemPortfolio = itemPortfolio; }
    public Long getCantidadInicial() { return cantidadInicial; }
    public void setCantidadInicial(Long cantidadInicial) { this.cantidadInicial = cantidadInicial; }
    public Long getCantidadRestante() { return cantidadRestante; }
    public void setCantidadRestante(Long cantidadRestante) { this.cantidadRestante = cantidadRestante; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}