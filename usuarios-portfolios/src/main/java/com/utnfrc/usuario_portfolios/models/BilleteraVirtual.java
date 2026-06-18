package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "billeteras_virtuales")
public class BilleteraVirtual implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String alias;

    private Long numeroCBU;

    private Long dineroTotal;
    private Long dineroLibre;
    private Long dineroInvertido;

    @OneToOne(mappedBy = "billeteraVirtual")
    @JsonBackReference
    private Usuarios usuario;

    public BilleteraVirtual() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Long getNumeroCBU() {
        return numeroCBU;
    }

    public void setNumeroCBU(Long numeroCBU) {
        this.numeroCBU = numeroCBU;
    }

    public Long getDineroTotal() {
        return dineroTotal;
    }

    public void setDineroTotal(Long dineroTotal) {
        this.dineroTotal = dineroTotal;
    }

    public Long getDineroLibre() {
        return dineroLibre;
    }

    public void setDineroLibre(Long dineroLibre) {
        this.dineroLibre = dineroLibre;
    }

    public Long getDineroInvertido() {
        return dineroInvertido;
    }

    public void setDineroInvertido(Long dineroInvertido) {
        this.dineroInvertido = dineroInvertido;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }
}