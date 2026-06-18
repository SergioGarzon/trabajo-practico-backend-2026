package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class BilleteraVirtual {
    private BigDecimal saldo = BigDecimal.ZERO;

    public BilleteraVirtual() {
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}