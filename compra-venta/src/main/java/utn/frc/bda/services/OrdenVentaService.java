package utn.frc.bda.services;

import org.springframework.stereotype.Service;

import utn.frc.bda.clients.UsuariosPortfoliosClient;
import utn.frc.bda.interfaces.OrdenVentaInterfaz;
import utn.frc.bda.models.OrdenVenta;
import utn.frc.bda.repositories.OrdenVentaRepository; // Deberás crear esta interfaz extendiendo JpaRepository

@Service
public class OrdenVentaService implements OrdenVentaInterfaz {

    private final OrdenVentaRepository repository;
    private final UsuariosPortfoliosClient portfolioClient;

    public OrdenVentaService(OrdenVentaRepository repository, UsuariosPortfoliosClient portfolioClient) {
        this.repository = repository;
        this.portfolioClient = portfolioClient;
    }

    @Override
    public OrdenVenta registrarOrdenVenta(String usuarioId, String simbolo, Long cantidad, Double precio, String jwtToken) {

        OrdenVenta nuevaOrden = new OrdenVenta();
        nuevaOrden.setUsuarioId(usuarioId);
        nuevaOrden.setSimboloAccion(simbolo);
        nuevaOrden.setCantidad(cantidad);
        nuevaOrden.setPrecio(precio);

        // Persistimos primero para que Hibernate genere el ID autoincremental
        nuevaOrden = repository.save(nuevaOrden);

        // Verificamos con el servicio de portfolio si tiene las acciones necesarias para la venta
        boolean validacionOk = portfolioClient.validarOrdenVenta(nuevaOrden.getId(), nuevaOrden.getSimboloAccion(), cantidad, jwtToken);

        if (validacionOk) {
            return nuevaOrden;
        } else {
            // Rollback manual: eliminamos la orden persistida si el portfolio rechaza la operación
            repository.delete(nuevaOrden);
            throw new RuntimeException("Operación rechazada: acciones insuficientes en el portfolio.");
        }
    }

    public boolean realizarVenta(Long idOrdenVenta, Double dineroObtenido, Long cantidadVendida) {
        boolean compraRealizada = portfolioClient.procesarVenta(idOrdenVenta, dineroObtenido, cantidadVendida);
        return  compraRealizada;
    }
}