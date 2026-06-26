package com.utnfrc.usuario_portfolios.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "items_portfolio")
@NoArgsConstructor
@Getter
@Setter
public class ItemPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- NUEVOS CONTADORES ---
    private Long cantidadLibre;
    private Long cantidadBloqueada;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    @JsonBackReference
    private Portfolio portfolio;

    @ManyToOne
    @JoinColumn(name = "accion_id")
    private Accion accion;

    
}