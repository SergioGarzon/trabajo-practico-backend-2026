package utn.frc.bda.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.frc.bda.clients.UsuariosPortfoliosClient;
import utn.frc.bda.models.OrdenCompra;
import utn.frc.bda.repositories.OrdenCompraRepository;
import utn.frc.bda.repositories.OrdenVentaRepository;

import utn.frc.bda.interfaces.OrdenCompraInterface;

@Service
public class OrdenCompraService implements OrdenCompraInterface {
    private final OrdenCompraRepository repository;
    private final UsuariosPortfoliosClient portfolioClient;

    public OrdenCompraService(OrdenCompraRepository repository, UsuariosPortfoliosClient portfolioClient) {
        this.repository = repository;
        this.portfolioClient = portfolioClient;
    }

    @Transactional
    public OrdenCompra registrarOrdenCompra(String userId, String simboloAccion, Long cantidad, Double precioUnitario, String jwtToken) {

        OrdenCompra nuevaOrden = new OrdenCompra();
        nuevaOrden.setUsuarioId(userId);
        nuevaOrden.setSimboloAccion(simboloAccion);
        nuevaOrden.setCantidad(cantidad);
        nuevaOrden.setPrecio(precioUnitario);

        Double totalPagar = nuevaOrden.getCantidad() * nuevaOrden.getPrecio();

        boolean validacionOk = portfolioClient.validarOrdenCompra(nuevaOrden.getId(), totalPagar, jwtToken);

        if (validacionOk) {
            return repository.save(nuevaOrden);
        } else {
            // Si recibimos "RECHAZADO"
            throw new RuntimeException("Operación rechazada: Fondos o acciones insuficientes en el portfolio.");
        }
    }

    @Override
    public boolean realizarCompra(Long idOrdenCompra, Long cantidad, Double precio) {
        return portfolioClient.resolverOrdenCompra(idOrdenCompra, cantidad, precio);
    }
}
