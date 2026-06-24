package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "reservas_saldo")
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

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getMonto() { return monto; }
    public void setMonto(Long monto) { this.monto = monto; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public BilleteraVirtual getBilletera() { return billetera; }
    public void setBilletera(BilleteraVirtual billetera) { this.billetera = billetera; }
}
