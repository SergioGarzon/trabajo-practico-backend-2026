package com.utnfrc.usuario_portfolios.models;

import com.utnfrc.usuario_portfolios.excepciones.SaldoInsuficienteException;
import com.utnfrc.usuario_portfolios.excepciones.TransaccionInversionException;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "billeteras_virtuales")
public class BilleteraVirtual implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String alias;

    private Long numeroCBU;

    private Double dineroLibre;
    private Double dineroInvertido;
    private Double dineroBloqueado;

    @OneToOne(mappedBy = "billeteraVirtual")
    @JsonBackReference
    private Usuarios usuario;     

//    public BilleteraVirtual() {}
//
//    public void setAlias(String alias) {
//        this.alias = alias;
//    }
//    public void setNumeroCBU(Long numeroCBU) {
//        this.numeroCBU = numeroCBU;
//    }
//    public void setDineroLibre(Double dineroLibre) {
//        this.dineroLibre = dineroLibre;
//    }
//    public void setDineroInvertido(Double dineroInvertido) {
//        this.dineroInvertido = dineroInvertido;
//    }
//    public void setDineroBloqueado(Double dineroBloqueado) {
//        this.dineroBloqueado = dineroBloqueado;
//    }
//    public void setUsuario(Usuarios usuario) {
//        this.usuario = usuario;
//    }
//    public Usuarios getUsuario() {
//        return usuario;
//    }
//    public Long getId() {
//        return id;
//    }
//    public void setId(Long id) {
//        this.id = id;
//    }
//    public String getAlias() {
//        return alias;
//    }
//    public Double getDineroLibre() {
//        return dineroLibre;
//    }
//    public Double getDineroInvertido() {
//        return dineroInvertido;
//    }
//    public Double getDineroBloqueado() {
//        return dineroBloqueado;
//    }


    public void retirarDinero(Double cant){
        if (cant <= 0){
            throw new IllegalArgumentException("La cantidad a retirar debe ser mayor a 0");
        }
        if (cant > dineroLibre){
            throw new SaldoInsuficienteException("No tienes saldo suficiente para realizar el retiro POBRE");
        }
        setDineroLibre(dineroLibre - cant);
    }

    public void ingresarDinero(Double cant){
        if (cant <= 0){ throw new IllegalArgumentException("La cantidad a ingresar debe ser mayor a 0"); }
        setDineroLibre(dineroLibre + cant);
    }
}