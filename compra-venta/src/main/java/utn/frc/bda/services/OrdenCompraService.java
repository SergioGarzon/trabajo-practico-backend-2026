package utn.frc.bda.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.frc.bda.clients.UsuariosPortfoliosClient;
import utn.frc.bda.models.OrdenCompra;
import utn.frc.bda.repositories.OrdenCompraRepository;
import utn.frc.bda.repositories.OrdenVentaRepository;

import utn.frc.bda.interfaces.OrdenCompraInterface;

import java.util.List;

@Service
public class OrdenCompraService implements OrdenCompraInterface {
    private final OrdenCompraRepository repository;
    private final UsuariosPortfoliosClient portfolioClient;

    public OrdenCompraService(OrdenCompraRepository repository, UsuariosPortfoliosClient portfolioClient) {
        this.repository = repository;
        this.portfolioClient = portfolioClient;
    }

    public OrdenCompra registrarOrdenCompra(String userId, String simboloAccion, Long cantidad, Double precioUnitario, String jwtToken) {
        OrdenCompra nuevaOrden = new OrdenCompra();
        nuevaOrden.setUsuarioId(userId);
        nuevaOrden.setSimboloAccion(simboloAccion);
        nuevaOrden.setCantidad(cantidad);
        nuevaOrden.setPrecio(precioUnitario);

        // Persistimos primero para que Hibernate genere el ID autoincremental
        nuevaOrden = repository.save(nuevaOrden);

        try {
            Double totalPagar = nuevaOrden.getCantidad() * nuevaOrden.getPrecio();
            boolean validacionOk = portfolioClient.validarOrdenCompra(nuevaOrden.getId(), totalPagar, jwtToken);

            if (validacionOk) {
                return nuevaOrden;
            } else {
                // Rollback manual: eliminamos la orden si el portfolio rechaza la operación
                repository.delete(nuevaOrden);
                throw new RuntimeException("Operación rechazada: fondos insuficientes en la billetera.");
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            // Rollback manual ante cualquier error de comunicación con el servicio externo
            repository.delete(nuevaOrden);
            throw new RuntimeException("Error al comunicarse con el servicio de billetera: " + e.getMessage());
        }
    }

    @Override
    public boolean realizarCompra(Long idOrdenCompra, Long cantidad, Double precio) {
        // Nota: este método es parte de la interfaz OrdenCompraInterface (flujo sincrónico).
        // El Motor de Emparejamiento usa directamente EmparejamientoService, que pasa
        // usuarioId y simboloAccion. Aquí usamos strings vacíos como fallback
        // ya que este path no es invocado por el @Scheduled.
        return portfolioClient.resolverOrdenCompra(idOrdenCompra, cantidad, precio, "", "");
    }

    public List<OrdenCompra> obtenerOrdenCompras() {
        return repository.findAll();
    }

}
