package com.catedra.backend.compraventa.entity;

import jakarta.persistence.*;
import lombok.Data;


import lombok.EqualsAndHashCode;


import lombok.NoArgsConstructor;



@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "ordenes_compra")
public class OrdenCompra extends Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long cantidadPedida;

    @Column(nullable = false)
    private Long cantidadRestante;


}
