package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter

@Entity
@Table(name = "items_portfolio")
public class ItemPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cantidad; // <-- ¡AQUÍ SE GUARDA LA CANTIDAD!

    // Muchos items pertenecen a un solo Portfolio
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    // Muchos items pueden referenciar a la misma Acción del mercado
    @ManyToOne
    @JoinColumn(name = "accion_id")
    private Accion accion;

    // Getters y Setters
}

