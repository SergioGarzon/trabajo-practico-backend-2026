package com.utnfrc.usuario_portfolios.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_operaciones")
@Data // Si usas Lombok (si no, genera los getters, setters y constructores)
public class RegistroOperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con tu entidad Usuarios existente
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOperacion tipoOperacion;

    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    // Opcional: para registrar de cuánto fue el movimiento de dinero
    private Double monto;

    @PrePersist
    protected void onCreate() {
        this.fechaHora = LocalDateTime.now(); // Se setea la hora automáticamente al guardar
    }
}