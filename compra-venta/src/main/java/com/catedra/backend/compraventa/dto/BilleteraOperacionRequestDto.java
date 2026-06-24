package com.catedra.backend.compraventa.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;


import lombok.Data;


import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
public class BilleteraOperacionRequestDto {

    @Id
    @Column(name = "id", length = 36)
    private String usuarioId;


    private Double monto;
}
