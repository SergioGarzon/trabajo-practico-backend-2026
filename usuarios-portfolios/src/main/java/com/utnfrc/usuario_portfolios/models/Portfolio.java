package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class Portfolio {
    private String descripcion;

    public Portfolio() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}