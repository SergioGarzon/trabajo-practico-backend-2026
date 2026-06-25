package com.utnfrc.usuario_portfolios.models;

import com.utnfrc.usuario_portfolios.excepciones.SaldoInsuficienteException;
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

    private Long dineroLibre;
    private Long dineroInvertido;
    private Long dineroBloqueado;

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

    public Long getDineroBloqueado() {return dineroBloqueado;}
    public void setDineroBloqueado(Long dineroBloqueado) {this.dineroBloqueado = dineroBloqueado;}

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public void retirarDinero(Long cant){
        if (cant <= 0){
            throw new IllegalArgumentException("La cantidad a retirar debe ser mayor a 0");
        }
        if (cant > dineroLibre){
            throw new SaldoInsuficienteException("No tienes saldo suficiente para realizar el retiro POBRE");
        }
        setDineroLibre(dineroLibre - cant);
    }

    public void ingresarDinero(Long cant){
        if (cant <= 0){ throw new IllegalArgumentException("La cantidad a ingresar debe ser mayor a 0"); }
        setDineroLibre(dineroLibre + cant);
    }
}