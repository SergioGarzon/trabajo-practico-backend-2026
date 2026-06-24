package com.catedra.backend.compraventa.entity;

import jakarta.persistence.*;
import lombok.Data;


import lombok.EqualsAndHashCode;


import lombok.NoArgsConstructor;



@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ordenes_venta")
public class OrdenVenta extends Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long cantidadOriginal;

    @Column(nullable = false)
    private Long cantidadRestante;
}
