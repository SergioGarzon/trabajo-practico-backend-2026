package utn.frc.bda.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import utn.frc.bda.interfaces.OrdenCompraInterface;
import utn.frc.bda.interfaces.OrdenVentaInterfaz;
import utn.frc.bda.models.OrdenCompra;
import utn.frc.bda.models.OrdenVenta;
import utn.frc.bda.repositories.OrdenCompraRepository;
import utn.frc.bda.repositories.OrdenVentaRepository;

@Service
public class EmparejamientoService {

    private final OrdenVentaInterfaz ordenVentaInterfaz;
    private final OrdenCompraInterface ordenCompraInterface;
    private final OrdenVentaRepository ordenVentaRepository;
    private final OrdenCompraRepository ordenCompraRepository;

    public EmparejamientoService(OrdenVentaRepository ordenVentaRepository, OrdenCompraRepository ordenCompraRepository, OrdenVentaInterfaz ordenVentaInterfaz, OrdenCompraInterface ordenCompraInterface) {
        this.ordenVentaRepository = ordenVentaRepository;
        this.ordenVentaInterfaz = ordenVentaInterfaz;
        this.ordenCompraInterface = ordenCompraInterface;
        this.ordenCompraRepository = ordenCompraRepository;
    }

    @Transactional
    public void procesarNuevaCompra(OrdenCompra ordenCompra) {
        
        List<OrdenVenta> vendedoresCompatibles = ordenVentaRepository
                .findBySimboloAccionAndEstadoInAndPrecioLessThanEqualOrderByPrecioAscFechaCreacionAsc(
                        ordenCompra.getSimboloAccion(),
                        Arrays.asList("PENDIENTE", "PARCIAL"),
                        ordenCompra.getPrecio()
                );

        for (OrdenVenta ordenVenta : vendedoresCompatibles) {

            // no tiene sentido, si tiene 0 entoces su estado es "Total"
            if (ordenCompra.getCantidad() == 0) {
                break;
            }

            Long cantidadAComprar = 0L;
            Double precioAcordado = ordenVenta.getPrecio(); 

            if (ordenVenta.getCantidad() >= ordenCompra.getCantidad()) {
                cantidadAComprar = ordenCompra.getCantidad();
                
                ordenVenta.setCantidad(ordenVenta.getCantidad() - cantidadAComprar);
                ordenCompra.setCantidad(0L);
                
                ordenCompra.setEstado("COMPLETADA");
                ordenVenta.setEstado(ordenVenta.getCantidad() == 0 ? "COMPLETADA" : "PARCIAL");

                Double dineroTotal = cantidadAComprar * precioAcordado;
                ordenVentaInterfaz.realizarVenta(ordenVenta.getId(), dineroTotal, cantidadAComprar);
                ordenCompraInterface.realizarCompra(ordenCompra.getId(), cantidadAComprar, precioAcordado);

            } 
            else {
                cantidadAComprar = ordenVenta.getCantidad();
                
                ordenCompra.setCantidad(ordenCompra.getCantidad() - cantidadAComprar);
                ordenVenta.setCantidad(0L);

                Double dineroTotal = cantidadAComprar * precioAcordado;
                ordenVentaInterfaz.realizarVenta(ordenVenta.getId(), dineroTotal, cantidadAComprar);
                ordenCompraInterface.realizarCompra(ordenCompra.getId(), cantidadAComprar, precioAcordado);
                
                ordenVenta.setEstado("COMPLETADA");
                ordenCompra.setEstado("PARCIAL");
            }
            ordenVentaRepository.save(ordenVenta);
            ordenCompraRepository.save(ordenCompra);

            System.out.println("¡MATCH! Compradas " + cantidadAComprar + " acciones de " + 
                               ordenCompra.getSimboloAccion() + " a $" + precioAcordado);
        }
    
        if (ordenCompra.getCantidad() > 0 && !ordenCompra.getEstado().equals("PENDIENTE")) {
            ordenCompra.setEstado("PARCIAL");
            ordenCompraRepository.save(ordenCompra);
        }
    }
}