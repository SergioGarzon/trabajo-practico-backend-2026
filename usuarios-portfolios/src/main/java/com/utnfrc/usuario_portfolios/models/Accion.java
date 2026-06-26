package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Service;

@Getter 
@Setter
@NoArgsConstructor

@Entity
@Table(name = "ACCIONES")
public class Accion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String simbolo;
}
