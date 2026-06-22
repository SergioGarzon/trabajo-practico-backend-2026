package com.catedra.backend.compraventa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ordenes_compra")
public class OrdenCompra extends Orden {

    @Column(nullable = false)
    private Long cantidadPedida;
}
