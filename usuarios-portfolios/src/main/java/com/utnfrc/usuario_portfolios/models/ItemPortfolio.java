package com.utnfrc.usuario_portfolios.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "items_portfolio")
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

    public ItemPortfolio() {
        this.cantidadLibre = 0L;
        this.cantidadBloqueada = 0L;
    }

    // Getters y Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCantidadLibre() { return cantidadLibre; }
    public void setCantidadLibre(Long cantidadLibre) { this.cantidadLibre = cantidadLibre; }
    public Long getCantidadBloqueada() { return cantidadBloqueada; }
    public void setCantidadBloqueada(Long cantidadBloqueada) { this.cantidadBloqueada = cantidadBloqueada; }
    public Portfolio getPortfolio() { return portfolio; }
    public void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }
    public Accion getAccion() { return accion; }
    public void setAccion(Accion accion) { this.accion = accion; }
}