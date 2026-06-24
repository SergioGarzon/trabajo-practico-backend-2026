package com.catedra.backend.compraventa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "transacciones")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_compra_id", nullable = false)
    private OrdenCompra ordenCompra;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_venta_id", nullable = false)
    private OrdenVenta ordenVenta;

    @Column(nullable = false)
    private Long cantidadAcciones;

    @Column(nullable = false)
    private Double precioEjecucion;

    @Column(nullable = false)
    private Double montoTotal;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
