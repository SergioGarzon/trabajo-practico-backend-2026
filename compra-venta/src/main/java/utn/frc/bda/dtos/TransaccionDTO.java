package utn.frc.bda.dtos;

import java.time.LocalDateTime;

public class TransaccionDTO {

    private Long id;
    private Long idOrdenCompra;
    private Long idOrdenVenta;
    private String simboloAccion;
    private Long cantidad;
    private Double precioAcordado;
    private LocalDateTime fechaTransaccion;

    // --- Constructores ---
    public TransaccionDTO() {}

    public TransaccionDTO(Long id, Long idOrdenCompra, Long idOrdenVenta, String simboloAccion, Long cantidad, Double precioAcordado, LocalDateTime fechaTransaccion) {
        this.id = id;
        this.idOrdenCompra = idOrdenCompra;
        this.idOrdenVenta = idOrdenVenta;
        this.simboloAccion = simboloAccion;
        this.cantidad = cantidad;
        this.precioAcordado = precioAcordado;
        this.fechaTransaccion = fechaTransaccion;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdOrdenCompra() { return idOrdenCompra; }
    public void setIdOrdenCompra(Long idOrdenCompra) { this.idOrdenCompra = idOrdenCompra; }

    public Long getIdOrdenVenta() { return idOrdenVenta; }
    public void setIdOrdenVenta(Long idOrdenVenta) { this.idOrdenVenta = idOrdenVenta; }

    public String getSimboloAccion() { return simboloAccion; }
    public void setSimboloAccion(String simboloAccion) { this.simboloAccion = simboloAccion; }

    public Long getCantidad() { return cantidad; }
    public void setCantidad(Long cantidad) { this.cantidad = cantidad; }

    public Double getPrecioAcordado() { return precioAcordado; }
    public void setPrecioAcordado(Double precioAcordado) { this.precioAcordado = precioAcordado; }

    public LocalDateTime getFechaTransaccion() { return fechaTransaccion; }
    public void setFechaTransaccion(LocalDateTime fechaTransaccion) { this.fechaTransaccion = fechaTransaccion; }
}