package com.utnfrc.usuario_portfolios.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "usuarios")
public class Usuarios implements Serializable {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    private Long dni;

    private String nombre;
    private String apellido;
    private String domicilio;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "billetera_id", referencedColumnName = "id")
    @JsonManagedReference
    private BilleteraVirtual billeteraVirtual;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "portfolio_id", referencedColumnName = "id")
    private Portfolio portfolio;

    private String rol; 

    public Usuarios (){}
    public Usuarios(String id){
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setDni(Long dni){
        this.dni = dni;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setApellido(String apellido){
        this.apellido = apellido;
    }
    public void setDomicilio(String domicilio){
        this.domicilio = domicilio;
    }
    public void setRol(String rol){
        this.rol = rol;
    }
    public void setBilleteraVirtual(BilleteraVirtual billeteraVirtual){
        this.billeteraVirtual = billeteraVirtual;
    }
    public void setPortfolio(Portfolio portfolio){
        this.portfolio = portfolio;
    }
    public BilleteraVirtual getBilleteraVirtual() {
        return billeteraVirtual;
    }
    public  Portfolio getPortfolio() {
        return portfolio;
    }
    public String getRol() {
        return rol;
    }
    public String getDomicilio() {
        return domicilio;
    }
    public String getApellido() {
        return apellido;
    }
    public String getNombre() {
        return nombre;
    }
    public Long getDni() {
        return dni;
    }

}