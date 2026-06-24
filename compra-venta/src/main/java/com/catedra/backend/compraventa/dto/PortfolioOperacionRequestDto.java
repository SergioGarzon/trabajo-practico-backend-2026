package com.catedra.backend.compraventa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioOperacionRequestDto {

    @NotNull
    private Long usuarioId;

    @NotBlank
    private String simboloAccion;

    @NotNull
    private Long cantidad;
}
