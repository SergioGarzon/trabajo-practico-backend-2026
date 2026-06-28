package utn.frc.bda.dtos;

import lombok.Data;

@Data
public class NuevaOrdenCompraDTO {
    private String simbolo;
    private Long cantidad;
    private Double montoMaximo;
}