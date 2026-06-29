package utn.frc.bda.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orden_venta")
public class OrdenVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuarioId; // Obtenido del token
    private String simboloAccion;
    private Long cantidad;
    private Double precio;
    private String estado; // PENDIENTE, COMPLETADA, CANCELADA
    private LocalDateTime fechaCreacion;

    public OrdenVenta() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getUsuarioId() {return usuarioId;}
    public void setUsuarioId(String usuarioId) {this.usuarioId = usuarioId;}
    public String getSimboloAccion() {return simboloAccion;}
    public void setSimboloAccion(String simboloAccion) {this.simboloAccion = simboloAccion;}
    public Long getCantidad() {return cantidad;}
    public void setCantidad(Long cantidad) {this.cantidad = cantidad;}
    public Double getPrecio() {return precio;}
    public void setPrecio(Double precio) {this.precio = precio;}
    public String getEstado() {return estado;}
    public void setEstado(String estado) {this.estado = estado;}



}
