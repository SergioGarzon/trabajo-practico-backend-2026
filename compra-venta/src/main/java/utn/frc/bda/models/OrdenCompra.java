package utn.frc.bda.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter

@Entity
@Table(name = "orden_venta")
public class OrdenCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId; // Obtenido del token
    private String simboloAccion;
    private Long cantidad;
    private Double precio;
    private String estado; // PENDIENTE, COMPLETADA, CANCELADA
    private LocalDateTime fechaCreacion;

    public OrdenCompra() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }
}
