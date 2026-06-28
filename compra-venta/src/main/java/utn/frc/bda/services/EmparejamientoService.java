package utn.frc.bda.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import utn.frc.bda.models.OrdenCompra;
import utn.frc.bda.models.OrdenVenta;
import utn.frc.bda.repositories.OrdenCompraRepository;
import utn.frc.bda.repositories.OrdenVentaRepository;

@Service
public class EmparejamientoService {

    private final OrdenVentaRepository ordenVentaRepository;
    private final OrdenCompraRepository ordenCompraRepository;

    public EmparejamientoService(OrdenVentaRepository ordenVentaRepository, OrdenCompraRepository ordenCompraRepository) {
        this.ordenVentaRepository = ordenVentaRepository;
        this.ordenCompraRepository = ordenCompraRepository;
    }

    public void procesarNuevaCompra(OrdenCompra ordenCompra) {
        
        List<OrdenVenta> vendedoresCompatibles = ordenVentaRepository
                .findBySimboloAccionAndEstadoInAndPrecioLessThanEqualOrderByPrecioAscFechaCreacionAsc(
                        ordenCompra.getSimboloAccion(),
                        Arrays.asList("PENDIENTE", "PARCIAL"),
                        ordenCompra.getPrecio()
                );

        for (OrdenVenta ordenVenta : vendedoresCompatibles) {
            
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

            } 
            else {
                cantidadAComprar = ordenVenta.getCantidad();
                
                ordenCompra.setCantidad(ordenCompra.getCantidad() - cantidadAComprar);
                ordenVenta.setCantidad(0L);
                
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