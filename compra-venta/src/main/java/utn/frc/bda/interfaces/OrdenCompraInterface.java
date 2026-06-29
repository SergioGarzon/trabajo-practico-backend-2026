package utn.frc.bda.interfaces;

import utn.frc.bda.models.OrdenCompra;

import java.util.List;

public interface OrdenCompraInterface {
    OrdenCompra registrarOrdenCompra(String userId, String simboloAccion, Long cantidad, Double precioUnitario, String jwtToken);
    boolean realizarCompra(Long idOrdenCompra, Long cantidad, Double precio);
    List<OrdenCompra> obtenerOrdenCompras();
}
