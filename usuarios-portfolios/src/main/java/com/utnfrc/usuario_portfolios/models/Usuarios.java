package com.utnfrc.usuario_portfolios.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@Setter
@Getter
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

   
}