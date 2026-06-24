package com.catedra.backend.compraventa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ordenes_venta")
public class OrdenVenta extends Orden {

    @Column(nullable = false)
    private Long cantidadOriginal;

    @Column(nullable = false)
    private Long cantidadRestante;
}
