package utn.frc.bda.services;

import org.springframework.stereotype.Service;
import utn.frc.bda.models.OrdenVenta;
import utn.frc.bda.repositories.OrdenVentaRepository; // Deberás crear esta interfaz extendiendo JpaRepository
import utn.frc.bda.clients.UsuariosPortfoliosClient;

@Service
public class OrdenVentaService {

    private final OrdenVentaRepository repository;
    private final UsuariosPortfoliosClient portfolioClient;

    public OrdenVentaService(OrdenVentaRepository repository, UsuariosPortfoliosClient portfolioClient) {
        this.repository = repository;
        this.portfolioClient = portfolioClient;
    }

    public OrdenVenta registrarOrdenVenta(String usuarioId, String simbolo, int cantidad, double precio, String jwtToken) {

        OrdenVenta nuevaOrden = new OrdenVenta();
        nuevaOrden.setUsuarioId(usuarioId);
        nuevaOrden.setSimboloAccion(simbolo);
        nuevaOrden.setCantidad(cantidad);
        nuevaOrden.setPrecio(precio);

        // 1. Nos comunicamos con el servicio de portfolios
        // (En una venta, el otro servicio debería verificar si el usuario tiene la cantidad de acciones necesarias)
        boolean validacionOk = portfolioClient.validarOperacion(Long , jwtToken);

        // 2. Si recibimos el "OK"
        if (validacionOk) {
            OrdenVenta nuevaOrden = new OrdenVenta();
            nuevaOrden.setUsuarioId(usuarioId);
            nuevaOrden.setSimboloAccion(simbolo);
            nuevaOrden.setCantidad(cantidad);
            nuevaOrden.setPrecio(precio);

            // 3. Guardamos la orden de venta
            return repository.save(nuevaOrden);
        } else {
            // Si recibimos "RECHAZADO"
            throw new RuntimeException("Operación rechazada: Fondos o acciones insuficientes en el portfolio.");
        }
    }
}