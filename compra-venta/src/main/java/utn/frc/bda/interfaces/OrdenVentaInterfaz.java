package utn.frc.bda.interfaces;

import utn.frc.bda.models.OrdenVenta;

public interface OrdenVentaInterfaz {
    OrdenVenta registrarOrdenVenta(String usuarioId, String simbolo, Long cantidad, double precio, String jwtToken);
    boolean realizarVenta(Long idOrdenVenta, Double dineroTotal, Long cantidadVendida);
}
