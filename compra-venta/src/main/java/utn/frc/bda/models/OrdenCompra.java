package utn.frc.bda.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@ToString
@Entity
@Table(name = "orden_compra")
public class OrdenCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuarioId; // Obtenido del token
    private String simboloAccion;
    private Long cantidad;
    private Double precio;
    private String estado; // PENDIENTE, COMPLETADA, CANCELADA
    private LocalDateTime fechaCreacion;

    public OrdenCompra() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsuarioId() {return usuarioId;}
    public void setUsuarioId(String usuarioId) {this.usuarioId = usuarioId;}
    public String getSimboloAccion() {return simboloAccion;}
    public void setSimboloAccion(String simboloAccion) {
        this.simboloAccion = simboloAccion;
    }

    public void setCantidad(Long cantidad) {this.cantidad = cantidad;}
    public Double getPrecio() {return precio;}
    public void setPrecio(Double precio) {this.precio = precio;}
    public String getEstado() {return estado;}
    public void setEstado(String estado) {this.estado = estado;}

}
