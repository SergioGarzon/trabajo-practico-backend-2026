package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.*;
import java.util.UUID;

import lombok.Setter;
import lombok.Getter;

@Entity
@Table(name = "orden_compra")
@Setter
@Getter
public class OrdenCompra {

    @Id
    private Long id;
    private Double monto;


    private String estado; // "PENDIENTE", "CONFIRMADA", "RECHAZADA"

    @ManyToOne
    @JoinColumn(name = "billetera_id")
    private BilleteraVirtual billetera;


    public OrdenCompra(Long idOrdenCompra) {
        this.id = idOrdenCompra;
        this.estado = "PENDIENTE";
    }    
}
