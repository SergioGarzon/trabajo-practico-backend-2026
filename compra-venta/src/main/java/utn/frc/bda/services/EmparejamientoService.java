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
        
        // 1. Buscamos vendedores con los nuevos nombres de atributos
        List<OrdenVenta> vendedoresCompatibles = ordenVentaRepository
                .findBySimboloAccionAndEstadoInAndPrecioLessThanEqualOrderByPrecioAscFechaCreacionAsc(
                        ordenCompra.getSimboloAccion(),
                        Arrays.asList("PENDIENTE", "PARCIAL"),
                        ordenCompra.getPrecio()
                );

        // 2. Bucle de emparejamiento
        for (OrdenVenta ordenVenta : vendedoresCompatibles) {
            
            if (ordenCompra.getCantidad() == 0) {
                break; // Ya compramos todo
            }

            Long cantidadAComprar = 0L;
            Double precioAcordado = ordenVenta.getPrecio(); 

            // CASO A: El vendedor tiene MÁS o IGUAL cantidad de la que necesito
            if (ordenVenta.getCantidad() >= ordenCompra.getCantidad()) {
                cantidadAComprar = ordenCompra.getCantidad();
                
                // Restamos a la venta y la compra queda en 0
                ordenVenta.setCantidad(ordenVenta.getCantidad() - cantidadAComprar);
                ordenCompra.setCantidad(0L);
                
                ordenCompra.setEstado("COMPLETADA");
                ordenVenta.setEstado(ordenVenta.getCantidad() == 0 ? "COMPLETADA" : "PARCIAL");

            } 
            // CASO B: El vendedor tiene MENOS cantidad de la que necesito
            else {
                cantidadAComprar = ordenVenta.getCantidad();
                
                // Le compro todo al vendedor y mi orden resta lo comprado
                ordenCompra.setCantidad(ordenCompra.getCantidad() - cantidadAComprar);
                ordenVenta.setCantidad(0L);
                
                ordenVenta.setEstado("COMPLETADA");
                ordenCompra.setEstado("PARCIAL");
            }

            // Guardamos el estado actual
            ordenVentaRepository.save(ordenVenta);
            ordenCompraRepository.save(ordenCompra);

            System.out.println("¡MATCH! Compradas " + cantidadAComprar + " acciones de " + 
                               ordenCompra.getSimboloAccion() + " a $" + precioAcordado);
        }
        
        // Si no se completó del todo pero compramos algo, pasamos la orden a PARCIAL
        // NOTA: Para que esto sea exacto sin "cantidadInicial", validamos si el estado ya no es PENDIENTE.
        // Pero lo ideal es que agreguen cantidadInicial a los modelos.
        if (ordenCompra.getCantidad() > 0 && !ordenCompra.getEstado().equals("PENDIENTE")) {
            ordenCompra.setEstado("PARCIAL");
            ordenCompraRepository.save(ordenCompra);
        }
    }
}