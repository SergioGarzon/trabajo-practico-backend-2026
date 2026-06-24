package com.utnfrc.usuarios_portfolios_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements Serializable {

    @Id
    private Long dni;

    // Identificador único de Keycloak
    @Column(name = "keycloak_id", unique = true, nullable = false)
    private String keycloakId;

    private String nombre;
    private String apellido;
    private String domicilio;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "billetera_id", referencedColumnName = "id")
    @JsonManagedReference
    private BilleteraVirtual billeteraVirtual;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "portfolio_id", referencedColumnName = "id")
    @JsonManagedReference
    private Portfolio portfolio;

    private String rol;
}

