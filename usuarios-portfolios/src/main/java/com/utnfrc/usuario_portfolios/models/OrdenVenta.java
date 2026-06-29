package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "ordenes_venta")
@Getter 
@Setter
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
}