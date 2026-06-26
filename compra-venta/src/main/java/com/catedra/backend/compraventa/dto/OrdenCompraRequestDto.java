package com.catedra.backend.compraventa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrdenCompraRequestDto {

    @NotNull
    private Long usuarioId;

    @NotBlank
    private String simboloAccion;

    @NotNull
    @Positive
    private Double precioPorAccion;

    @NotNull
    @Positive
    private Long cantidadPedida;
}
