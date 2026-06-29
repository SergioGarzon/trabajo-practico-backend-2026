package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.*;
import java.util.UUID;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ordenes_venta")
@Setter
@Getter
@NoArgsConstructor
public class OrdenVenta {

    @Id
    private Long id; // UUID de la transacción

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