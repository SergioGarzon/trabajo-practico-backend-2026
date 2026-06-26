package com.catedra.backend.compraventa.service;

import com.catedra.backend.compraventa.client.BilleteraClient;
import com.catedra.backend.compraventa.client.VentaClient;
import com.catedra.backend.compraventa.dto.ResolucionVentaDTO;
import com.catedra.backend.compraventa.dto.SolicitudDineroDTO;
import com.catedra.backend.compraventa.exception.MicroservicioFallaException;
import com.catedra.backend.compraventa.models.OrdenCompra;
import com.catedra.backend.compraventa.models.OrdenVenta;
import com.catedra.backend.compraventa.models.Transaccion;
import com.catedra.backend.compraventa.models.enums.EstadoOrden;
import com.catedra.backend.compraventa.repository.OrdenCompraRepository;
import com.catedra.backend.compraventa.repository.OrdenVentaRepository;
import com.catedra.backend.compraventa.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio responsable de emparejar órdenes de compra y venta compatibles.
 *
 * Flujo de liquidación:
 * 1. Persiste la transacción y actualiza los estados de ambas órdenes.
 * 2. Llama a BilleteraClient → /api/billetera/operacion/resolver  (liquida al COMPRADOR)
 * 3. Llama a VentaClient     → /api/ventas/procesar               (liquida al VENDEDOR)
 *
 * Las dos llamadas son independientes (no hay XA). Si una falla, se lanza
 * MicroservicioFallaException para que el llamador decida el manejo de error.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmparejamientoService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenVentaRepository ordenVentaRepository;
    private final TransaccionRepository transaccionRepository;
    private final BilleteraClient billeteraClient;
    private final VentaClient ventaClient;

    // -------------------------------------------------------------------------
    // Puntos de entrada públicos
    // -------------------------------------------------------------------------

    /**
     * Procesa una orden de compra recién creada buscando ventas compatibles.
     * Si no hay contraparte disponible la orden queda en estado PENDIENTE.
     */
    @Transactional
    public void procesarOrdenCompra(OrdenCompra ordenCompra) {
        List<OrdenVenta> ventasCompatibles = ordenVentaRepository.findVentasCompatibles(
                ordenCompra.getSimboloAccion(),
                ordenCompra.getPrecioPorAccion(),
                List.of(EstadoOrden.PENDIENTE, EstadoOrden.PARCIAL)
        );

        if (ventasCompatibles.isEmpty()) {
            log.info("Sin contraparte para compra #{} de {}. Queda PENDIENTE.",
                    ordenCompra.getId(), ordenCompra.getSimboloAccion());
            return;
        }

        for (OrdenVenta ordenVenta : ventasCompatibles) {
            if (ordenCompra.getCantidadRestante() <= 0) {
                break;
            }
            ejecutarMatch(ordenCompra, ordenVenta);
        }
    }

    /**
     * Procesa una orden de venta recién creada buscando compras compatibles.
     * Si no hay contraparte disponible la orden queda en estado PENDIENTE.
     */
    @Transactional
    public void procesarOrdenVenta(OrdenVenta ordenVenta) {
        List<OrdenCompra> comprasCompatibles = ordenCompraRepository.findComprasCompatibles(
                ordenVenta.getSimboloAccion(),
                ordenVenta.getPrecioPorAccion(),
                List.of(EstadoOrden.PENDIENTE, EstadoOrden.PARCIAL)
        );

        if (comprasCompatibles.isEmpty()) {
            log.info("Sin contraparte para venta #{} de {}. Queda PENDIENTE.",
                    ordenVenta.getId(), ordenVenta.getSimboloAccion());
            return;
        }

        for (OrdenCompra ordenCompra : comprasCompatibles) {
            if (ordenVenta.getCantidadRestante() <= 0) {
                break;
            }
            ejecutarMatch(ordenCompra, ordenVenta);
        }
    }

    // -------------------------------------------------------------------------
    // Lógica de emparejamiento
    // -------------------------------------------------------------------------

    /**
     * Ejecuta el match entre una orden de compra y una orden de venta.
     *
     * Pasos:
     *  1. Calcula cantidad y precio de ejecución.
     *  2. Persiste la Transaccion.
     *  3. Actualiza cantidades restantes y estados de ambas órdenes.
     *  4. Llama a billeteraClient para liquidar al COMPRADOR (atómico).
     *  5. Llama a ventaClient para liquidar al VENDEDOR (atómico).
     */
    private void ejecutarMatch(OrdenCompra ordenCompra, OrdenVenta ordenVenta) {
        // 1. Cálculo de la transacción
        Long cantidadTransaccion = Math.min(
                ordenCompra.getCantidadRestante(),
                ordenVenta.getCantidadRestante()
        );
        // El precio de ejecución es siempre el de la orden de venta (precio pactado)
        Double precioEjecucion = ordenVenta.getPrecioPorAccion();
        Double montoTransaccion = precioEjecucion * cantidadTransaccion;

        log.info("Ejecutando match: compra #{} + venta #{} | símbolo={} | cantidad={} | precio={}",
                ordenCompra.getId(), ordenVenta.getId(),
                ordenCompra.getSimboloAccion(), cantidadTransaccion, precioEjecucion);

        // 2. Persistir la transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setOrdenCompra(ordenCompra);
        transaccion.setOrdenVenta(ordenVenta);
        transaccion.setCantidadAcciones(cantidadTransaccion);
        transaccion.setPrecioEjecucion(precioEjecucion);
        transaccion.setMontoTotal(montoTransaccion);
        transaccion.setFecha(LocalDateTime.now());
        Transaccion transaccionGuardada = transaccionRepository.save(transaccion);

        // 3. Actualizar cantidades y estados de las órdenes
        ordenCompra.setCantidadRestante(ordenCompra.getCantidadRestante() - cantidadTransaccion);
        ordenVenta.setCantidadRestante(ordenVenta.getCantidadRestante() - cantidadTransaccion);

        actualizarEstadoOrden(ordenCompra);
        actualizarEstadoOrden(ordenVenta);

        ordenCompraRepository.save(ordenCompra);
        ordenVentaRepository.save(ordenVenta);

        // 4. Liquidar al COMPRADOR → usuarios-portfolios: /api/billetera/operacion/resolver
        //    estadoAccion = "CONFIRMAR": descuenta el dinero y acredita las acciones.
        try {
            SolicitudDineroDTO solicitudComprador = new SolicitudDineroDTO(
                    String.valueOf(transaccionGuardada.getId()),   // idTransaccion
                    montoTransaccion.longValue(),                  // monto en ARS
                    "CONFIRMAR",                                   // estadoAccion
                    ordenCompra.getSimboloAccion(),                // simbolo
                    cantidadTransaccion                            // cantidad de acciones
            );
            billeteraClient.resolverOperacion(solicitudComprador);
            log.info("Liquidación de COMPRADOR exitosa para transacción #{}.", transaccionGuardada.getId());
        } catch (Exception e) {
            log.error("Error al liquidar COMPRADOR para transacción #{}: {}",
                    transaccionGuardada.getId(), e.getMessage());
            throw new MicroservicioFallaException(
                    "usuarios-portfolios/billetera", "Error al liquidar al comprador: " + e.getMessage());
        }

        // 5. Liquidar al VENDEDOR → usuarios-portfolios: /api/ventas/procesar
        //    Descuenta las acciones del portfolio y acredita el dinero al vendedor.
        try {
            ResolucionVentaDTO resolucionVendedor = new ResolucionVentaDTO(
                    String.valueOf(ordenVenta.getId()),   // idOrdenVenta
                    cantidadTransaccion,                  // cantidadVendida
                    montoTransaccion.longValue()          // dineroObtenido en ARS
            );
            ventaClient.procesarVenta(resolucionVendedor);
            log.info("Liquidación de VENDEDOR exitosa para orden de venta #{}.", ordenVenta.getId());
        } catch (Exception e) {
            log.error("Error al liquidar VENDEDOR para orden de venta #{}: {}",
                    ordenVenta.getId(), e.getMessage());
            throw new MicroservicioFallaException(
                    "usuarios-portfolios/ventas", "Error al liquidar al vendedor: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void actualizarEstadoOrden(OrdenCompra orden) {
        if (orden.getCantidadRestante() == 0) {
            orden.setEstado(EstadoOrden.COMPLETADA);
            orden.setFechaFinalizacion(LocalDateTime.now());
        } else {
            orden.setEstado(EstadoOrden.PARCIAL);
        }
    }

    private void actualizarEstadoOrden(OrdenVenta orden) {
        if (orden.getCantidadRestante() == 0) {
            orden.setEstado(EstadoOrden.COMPLETADA);
            orden.setFechaFinalizacion(LocalDateTime.now());
        } else {
            orden.setEstado(EstadoOrden.PARCIAL);
        }
    }
}
