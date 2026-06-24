package com.catedra.backend.compraventa.entity;

import com.catedra.backend.compraventa.entity.enums.EstadoOrden;
import jakarta.persistence.*;
import lombok.Data;


import lombok.NoArgsConstructor;



import java.time.LocalDateTime;

@NoArgsConstructor
@MappedSuperclass
public abstract class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String usuarioId;

    @Column(nullable = false)
    private String simboloAccion;

    @Column(nullable = false)
    private Double precioPorAccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = true)
    private LocalDateTime fechaFinalizacion;
}
