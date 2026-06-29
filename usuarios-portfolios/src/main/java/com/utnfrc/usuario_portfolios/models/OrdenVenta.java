package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.*;
import java.util.UUID;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ordenes_venta")

@NoArgsConstructor
public class OrdenVenta {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_portfolio_id")
    private ItemPortfolio itemPortfolio;

    private Long cantidadInicial;
    private Long cantidadRestante; // Lo que falta vender
    private String estado; // "PENDIENTE", "PARCIAL", "COMPLETADA", "CANCELADA"

    public OrdenVenta(Long id) {
        this.id = id;
        this.estado = "PENDIENTE";
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setCantidadInicial(Long cantidadInicial) {
        this.cantidadInicial = cantidadInicial;
    }

    public void setCantidadRestante(Long cantidadRestante) {
        this.cantidadRestante = cantidadRestante;
    }

    public void setItemPortfolio(ItemPortfolio itemPortfolio) {
        this.itemPortfolio = itemPortfolio;
    }

    public ItemPortfolio getItemPortfolio() {
        return this.itemPortfolio;
    }
    public  Long getCantidadInicial() {
        return this.cantidadInicial;
    }
    public  Long getCantidadRestante() {
        return this.cantidadRestante;
    }

}