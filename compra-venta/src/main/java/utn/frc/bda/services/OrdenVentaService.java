package utn.frc.bda.services;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Service;
import utn.frc.bda.interfaces.OrdenVentaInterfaz;
import utn.frc.bda.models.OrdenVenta;
import utn.frc.bda.repositories.OrdenVentaRepository; // Deberás crear esta interfaz extendiendo JpaRepository
import utn.frc.bda.clients.UsuariosPortfoliosClient;

@Service
public class OrdenVentaService implements OrdenVentaInterfaz {

    private final OrdenVentaRepository repository;
    private final UsuariosPortfoliosClient portfolioClient;

    public OrdenVentaService(OrdenVentaRepository repository, UsuariosPortfoliosClient portfolioClient) {
        this.repository = repository;
        this.portfolioClient = portfolioClient;
    }

    public OrdenVenta registrarOrdenVenta(String usuarioId, String simbolo, Long cantidad, double precio, String jwtToken) {

        OrdenVenta nuevaOrden = new OrdenVenta();
        nuevaOrden.setUsuarioId(usuarioId);
        nuevaOrden.setSimboloAccion(simbolo);
        nuevaOrden.setCantidad(cantidad);
        nuevaOrden.setPrecio(precio);

        // Verificamos con el servicio de Billetera Virtual si tiene las acciones necesarias para le venta
        boolean validacionOk = portfolioClient.validarOrdenVenta(nuevaOrden.getSimboloAccion(), cantidad, jwtToken);

        // 2. Si recibimos el "OK"
        if (validacionOk) {
            return repository.save(nuevaOrden);
        } else {
            // Si recibimos "RECHAZADO"
            throw new RuntimeException("Operación rechazada: Fondos o acciones insuficientes en el portfolio.");
        }
    }

    public boolean realizarVenta(Long idOrdenVenta, Double dineroObtenido, Long cantidadVendida) {

        boolean compraRealizada = portfolioClient.procesarVenta(idOrdenVenta, dineroObtenido, cantidadVendida);
        return  compraRealizada;
    }
}