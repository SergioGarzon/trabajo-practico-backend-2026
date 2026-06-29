package utn.frc.bda.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transacciones")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Vinculamos la transacción con la orden de compra original
    @ManyToOne
    @JoinColumn(name = "orden_compra_id", nullable = false)
    private OrdenCompra ordenCompra;

    // Vinculamos la transacción con la orden de venta original
    @ManyToOne
    @JoinColumn(name = "orden_venta_id", nullable = false)
    private OrdenVenta ordenVenta;

    @Column(name = "simbolo_accion", nullable = false)
    private String simboloAccion;

    @Column(nullable = false)
    private Long cantidad;

    @Column(name = "precio_acordado", nullable = false)
    private Double precioAcordado;

    @Column(name = "fecha_transaccion")
    private LocalDateTime fechaTransaccion;

    // --- Constructor por defecto ---
    public Transaccion() {
        this.fechaTransaccion = LocalDateTime.now(); // Se setea la fecha automáticamente
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OrdenCompra getOrdenCompra() { return ordenCompra; }
    public void setOrdenCompra(OrdenCompra ordenCompra) { this.ordenCompra = ordenCompra; }

    public OrdenVenta getOrdenVenta() { return ordenVenta; }
    public void setOrdenVenta(OrdenVenta ordenVenta) { this.ordenVenta = ordenVenta; }

    public String getSimboloAccion() { return simboloAccion; }
    public void setSimboloAccion(String simboloAccion) { this.simboloAccion = simboloAccion; }

    public Long getCantidad() { return cantidad; }
    public void setCantidad(Long cantidad) { this.cantidad = cantidad; }

    public Double getPrecioAcordado() { return precioAcordado; }
    public void setPrecioAcordado(Double precioAcordado) { this.precioAcordado = precioAcordado; }

    public LocalDateTime getFechaTransaccion() { return fechaTransaccion; }
    public void setFechaTransaccion(LocalDateTime fechaTransaccion) { this.fechaTransaccion = fechaTransaccion; }
}