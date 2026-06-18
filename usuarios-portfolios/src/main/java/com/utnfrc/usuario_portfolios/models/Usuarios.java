package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usuarios")
public class Usuarios implements Serializable {

    @Id
    private Long dni;

    private String nombre;
    private String apellido;
    private String domicilio;

    @Embedded
    private BilleteraVirtual billeteraVirtual;

    @Embedded
    private Portfolio portfolio;

    private String rol;

    public Usuarios() {
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public BilleteraVirtual getBilleteraVirtual() {
        return billeteraVirtual;
    }

    public void setBilleteraVirtual(BilleteraVirtual billeteraVirtual) {
        this.billeteraVirtual = billeteraVirtual;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
