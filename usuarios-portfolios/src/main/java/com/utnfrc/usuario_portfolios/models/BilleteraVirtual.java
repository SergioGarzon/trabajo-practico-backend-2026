package com.utnfrc.usuario_portfolios.models;

import com.utnfrc.usuario_portfolios.excepciones.SaldoInsuficienteException;
import com.utnfrc.usuario_portfolios.excepciones.TransaccionInversionException;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "billeteras_virtuales")
@NoArgsConstructor
@Getter
@Setter
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