package com.catedra.backend.compraventa.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BilleteraOperacionRequestDto {

    @NotNull
    private Long usuarioId;

    @NotNull
    @Positive
    private Double monto;
}
