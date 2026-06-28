package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.*;
import java.util.UUID;

import lombok.Setter;
import lombok.Getter;

@Entity
@Table(name = "reservas_saldo")
@Setter
@Getter
public class ReservaSaldo {

    @Id
    private String id;
    private Long monto;

    private String estado; // "PENDIENTE", "CONFIRMADA", "RECHAZADA"

    @ManyToOne
    @JoinColumn(name = "billetera_id")
    private BilleteraVirtual billetera;


    public ReservaSaldo() {
        this.id = UUID.randomUUID().toString();
        this.estado = "PENDIENTE";
    }    
}
