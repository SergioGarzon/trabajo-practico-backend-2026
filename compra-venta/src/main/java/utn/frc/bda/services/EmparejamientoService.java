package utn.frc.bda.services;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import utn.frc.bda.clients.UsuariosPortfoliosClient;
import utn.frc.bda.interfaces.OrdenCompraInterface;
import utn.frc.bda.interfaces.OrdenVentaInterfaz;
import utn.frc.bda.models.OrdenCompra;
import utn.frc.bda.models.OrdenVenta;
import utn.frc.bda.models.Transaccion;
import utn.frc.bda.repositories.OrdenCompraRepository;
import utn.frc.bda.repositories.OrdenVentaRepository;
import utn.frc.bda.repositories.TransaccionRepository;

@Service
public class EmparejamientoService {

    private static final Logger log = LoggerFactory.getLogger(EmparejamientoService.class);

    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenVentaRepository ordenVentaRepository;
    private final TransaccionRepository transaccionRepository;
    private final UsuariosPortfoliosClient portfolioClient;

    // Mantenemos las interfaces del flujo de alta de órdenes (usado en procesarNuevaCompra)
    private final OrdenVentaInterfaz ordenVentaInterfaz;
    private final OrdenCompraInterface ordenCompraInterface;

    public EmparejamientoService(
            OrdenCompraRepository ordenCompraRepository,
            OrdenVentaRepository ordenVentaRepository,
            TransaccionRepository transaccionRepository,
            UsuariosPortfoliosClient portfolioClient,
            OrdenVentaInterfaz ordenVentaInterfaz,
            OrdenCompraInterface ordenCompraInterface) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.ordenVentaRepository = ordenVentaRepository;
        this.transaccionRepository = transaccionRepository;
        this.portfolioClient = portfolioClient;
        this.ordenVentaInterfaz = ordenVentaInterfaz;
        this.ordenCompraInterface = ordenCompraInterface;
    }

    /**
     * Motor de Emparejamiento (Matchmaking).
     * Se ejecuta automáticamente cada 10 segundos en segundo plano.
     * Busca todas las órdenes de compra PENDIENTES e intenta emparejarlas
     * con órdenes de venta PENDIENTES del mismo símbolo cuyo precio sea
     * menor o igual al de la compra.
     */
    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void procesarEmparejamientos() {
        log.info("[Matchmaking] Iniciando ciclo de emparejamiento...");

        // 1. Obtener todas las órdenes de compra en estado PENDIENTE
        List<OrdenCompra> comprasPendientes = ordenCompraRepository.findByEstado("PENDIENTE");

        if (comprasPendientes.isEmpty()) {
            log.info("[Matchmaking] No hay órdenes de compra pendientes. Ciclo finalizado.");
            return;
        }

        log.info("[Matchmaking] Compras pendientes encontradas: {}", comprasPendientes.size());

        // 2. Iterar sobre cada orden de compra
        for (OrdenCompra compra : comprasPendientes) {

            // Obtener ventas pendientes del mismo símbolo
            List<OrdenVenta> ventasPendientes = ordenVentaRepository
                    .findBySimboloAccionAndEstado(compra.getSimboloAccion(), "PENDIENTE");

            // 3. Iterar sobre las ventas buscando un MATCH
            for (OrdenVenta venta : ventasPendientes) {

                // Condición de MATCH: el precio de la venta debe ser <= al precio de la compra
                if (venta.getPrecio() > compra.getPrecio()) {
                    continue; // Esta venta es demasiado cara, pasar a la siguiente
                }

                // --- ¡MATCH ENCONTRADO! ---

                // El precio acordado es el de la venta (favorece al comprador)
                Double precioAcordado = venta.getPrecio();

                // La cantidad a intercambiar es el mínimo entre ambas órdenes
                Long cantidadAIntercambiar = Math.min(compra.getCantidad(), venta.getCantidad());

                // El monto total de la operación
                Double montoTotal = precioAcordado * cantidadAIntercambiar;
                
                Double montoSobrante = (compra.getPrecio() - precioAcordado) * cantidadAIntercambiar;

                log.info("[Matchmaking] Match encontrado! Compra#{} <-> Venta#{} | Símbolo: {} | Cantidad: {} | Precio: {}",
                        compra.getId(), venta.getId(), compra.getSimboloAccion(), cantidadAIntercambiar, precioAcordado);

                // 4. Llamar a los clientes del microservicio de portfolios/billetera
                boolean compraOk = portfolioClient.resolverOrdenCompra(
                        compra.getId(), cantidadAIntercambiar, montoTotal,
                        compra.getUsuarioId(), compra.getSimboloAccion(), montoSobrante);

                boolean ventaOk = portfolioClient.procesarVenta(
                        venta.getId(), montoTotal, cantidadAIntercambiar, venta.getUsuarioId());

                // 5. Solo persistir si AMBAS operaciones remotas fueron exitosas
                if (compraOk && ventaOk) {

                    // 5a. Crear y guardar la Transaccion que registra el match
                    Transaccion transaccion = new Transaccion();
                    transaccion.setOrdenCompra(compra);
                    transaccion.setOrdenVenta(venta);
                    transaccion.setSimboloAccion(compra.getSimboloAccion());
                    transaccion.setCantidad(cantidadAIntercambiar);
                    transaccion.setPrecioAcordado(precioAcordado);
                    transaccionRepository.save(transaccion);

                    // 5b. Actualizar cantidades restando lo intercambiado
                    compra.setCantidad(compra.getCantidad() - cantidadAIntercambiar);
                    venta.setCantidad(venta.getCantidad() - cantidadAIntercambiar);

                    // 5c. Si la cantidad llega a 0, cambiar estado a EJECUTADA
                    if (compra.getCantidad() == 0) {
                        compra.setEstado("EJECUTADA");
                    }
                    if (venta.getCantidad() == 0) {
                        venta.setEstado("EJECUTADA");
                    }

                    // 5d. Persistir ambas órdenes actualizadas
                    ordenCompraRepository.save(compra);
                    ordenVentaRepository.save(venta);

                    log.info("[Matchmaking] Transacción #{} guardada exitosamente. CompraRestante: {} | VentaRestante: {}",
                            transaccion.getId(), compra.getCantidad(), venta.getCantidad());

                } else {
                    log.warn("[Matchmaking] Fallo al confirmar match en servicio remoto. compraOk={} | ventaOk={}",
                            compraOk, ventaOk);
                }

                // 5e. Si la orden de compra ya fue totalmente ejecutada, salir del bucle de ventas
                if (compra.getCantidad() == 0) {
                    break;
                }
            }
        }

        log.info("[Matchmaking] Ciclo de emparejamiento finalizado.");
    }

    /**
     * Proceso de emparejamiento al momento de recibir una nueva orden de compra.
     * Se mantiene el flujo original (sincrónico) para respuesta inmediata al usuario.
     */
    @Transactional
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

            Long cantidadAComprar;
            Double precioAcordado = ordenVenta.getPrecio();

            if (ordenVenta.getCantidad() >= ordenCompra.getCantidad()) {
                cantidadAComprar = ordenCompra.getCantidad();

                ordenVenta.setCantidad(ordenVenta.getCantidad() - cantidadAComprar);
                ordenCompra.setCantidad(0L);

                ordenCompra.setEstado("COMPLETADA");
                ordenVenta.setEstado(ordenVenta.getCantidad() == 0 ? "COMPLETADA" : "PARCIAL");

                Double dineroTotal = cantidadAComprar * precioAcordado;
                ordenVentaInterfaz.realizarVenta(ordenVenta.getId(), dineroTotal, cantidadAComprar, ordenVenta.getUsuarioId());
                ordenCompraInterface.realizarCompra(ordenCompra.getId(), cantidadAComprar, precioAcordado);

            } else {
                cantidadAComprar = ordenVenta.getCantidad();

                ordenCompra.setCantidad(ordenCompra.getCantidad() - cantidadAComprar);
                ordenVenta.setCantidad(0L);

                Double dineroTotal = cantidadAComprar * precioAcordado;
                ordenVentaInterfaz.realizarVenta(ordenVenta.getId(), dineroTotal, cantidadAComprar, ordenVenta.getUsuarioId());
                ordenCompraInterface.realizarCompra(ordenCompra.getId(), cantidadAComprar, precioAcordado);

                ordenVenta.setEstado("COMPLETADA");
                ordenCompra.setEstado("PARCIAL");
            }

            ordenVentaRepository.save(ordenVenta);
            ordenCompraRepository.save(ordenCompra);

            log.info("[NuevaCompra] ¡MATCH! Compradas {} acciones de {} a ${}",
                    cantidadAComprar, ordenCompra.getSimboloAccion(), precioAcordado);
        }

        if (ordenCompra.getCantidad() > 0 && !ordenCompra.getEstado().equals("PENDIENTE")) {
            ordenCompra.setEstado("PARCIAL");
            ordenCompraRepository.save(ordenCompra);
        }
    }
}