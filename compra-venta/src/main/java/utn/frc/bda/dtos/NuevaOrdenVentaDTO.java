package utn.frc.bda.dtos;

import lombok.Data;

@Data
public class NuevaOrdenVentaDTO {
    private String simbolo;
    private Long cantidad;
    private Double montoMinimo; // Lo mínimo que acepta recibir por CADA acción
}