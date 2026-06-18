package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.io.Serializable;

@Entity
@Table(name = "billeteras_virtuales")
public class BilleteraVirtual implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal saldo = BigDecimal.ZERO;

    public BilleteraVirtual() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}