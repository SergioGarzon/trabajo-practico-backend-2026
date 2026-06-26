package com.catedra.backend.compraventa.service;

import com.catedra.backend.compraventa.client.BilleteraClient;
import com.catedra.backend.compraventa.client.VentaClient;
import com.catedra.backend.compraventa.dto.ResolucionVentaDTO;
import com.catedra.backend.compraventa.dto.SolicitudDineroDTO;
import com.catedra.backend.compraventa.models.OrdenCompra;
import com.catedra.backend.compraventa.models.OrdenVenta;
import com.catedra.backend.compraventa.models.Transaccion;
import com.catedra.backend.compraventa.models.enums.EstadoOrden;
import com.catedra.backend.compraventa.exception.BloqueoFondosException;
import com.catedra.backend.compraventa.exception.SinContraparteException;
import com.catedra.backend.compraventa.repository.OrdenCompraRepository;
import com.catedra.backend.compraventa.repository.OrdenVentaRepository;
import com.catedra.backend.compraventa.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmparejamientoService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenVentaRepository ordenVentaRepository;
    private final TransaccionRepository transaccionRepository;
    private final BilleteraClient billeteraClient;
    private final VentaClient ventaClient;

    @Transactional
    public void procesarOrdenCompra(OrdenCompra ordenCompra) {
        Double montoTotal = ordenCompra.getPrecioPorAccion() * ordenCompra.getCantidadRestante();

        bloquearFondosComprador(ordenCompra.getUsuarioId(), montoTotal);

        List<OrdenVenta> ventasCompatibles = ordenVentaRepository.findVentasCompatibles(
                ordenCompra.getSimboloAccion(),
                ordenCompra.getPrecioPorAccion(),
                List.of(EstadoOrden.PENDIENTE, EstadoOrden.PARCIAL)
        );

        if (ventasCompatibles.isEmpty()) {
            throw new SinContraparteException(ordenCompra.getSimboloAccion());
        }

        for (OrdenVenta ordenVenta : ventasCompatibles) {
            if (ordenCompra.getCantidadRestante() <= 0) {
                break;
            }
            ejecutarMatch(ordenCompra, ordenVenta);
        }
    }

    @Transactional
    public void procesarOrdenVenta(OrdenVenta ordenVenta) {
        List<OrdenCompra> comprasCompatibles = ordenCompraRepository.findComprasCompatibles(
                ordenVenta.getSimboloAccion(),
                ordenVenta.getPrecioPorAccion(),
                List.of(EstadoOrden.PENDIENTE, EstadoOrden.PARCIAL)
        );

        if (comprasCompatibles.isEmpty()) {
            return;
        }

        for (OrdenCompra ordenCompra : comprasCompatibles) {
            if (ordenVenta.getCantidadRestante() <= 0) {
                break;
            }
            ejecutarMatch(ordenCompra, ordenVenta);
        }
    }

    private void ejecutarMatch(OrdenCompra ordenCompra, OrdenVenta ordenVenta) {
        Long cantidadTransaccion = Math.min(ordenCompra.getCantidadRestante(), ordenVenta.getCantidadRestante());
        Double precioEjecucion = ordenVenta.getPrecioPorAccion();
        Double montoTransaccion = precioEjecucion * cantidadTransaccion;

        Transaccion transaccion = new Transaccion();
        transaccion.setOrdenCompra(ordenCompra);
        transaccion.setOrdenVenta(ordenVenta);
        transaccion.setCantidadAcciones(cantidadTransaccion);
        transaccion.setPrecioEjecucion(precioEjecucion);
        transaccion.setMontoTotal(montoTransaccion);
        transaccion.setFecha(LocalDateTime.now());
        transaccionRepository.save(transaccion);

        ordenCompra.setCantidadRestante(ordenCompra.getCantidadRestante() - cantidadTransaccion);
        ordenVenta.setCantidadRestante(ordenVenta.getCantidadRestante() - cantidadTransaccion);

        actualizarEstadoOrden(ordenCompra);
        actualizarEstadoOrden(ordenVenta);

        ordenCompraRepository.save(ordenCompra);
        ordenVentaRepository.save(ordenVenta);

        // 1. Resolver Comprador (Resta saldo, Suma acciones)
        SolicitudDineroDTO solicitud = new SolicitudDineroDTO();
        solicitud.setIdTransaccion(String.valueOf(ordenCompra.getId()));
        solicitud.setMonto(montoTransaccion.longValue());
        solicitud.setEstadoAccion("CONFIRMAR");
        solicitud.setSimbolo(ordenCompra.getSimboloAccion());
        solicitud.setCantidad(cantidadTransaccion);
        billeteraClient.resolverOperacion(solicitud);

        // 2. Resolver Vendedor (Resta acciones, Suma saldo)
        ResolucionVentaDTO resolucion = new ResolucionVentaDTO();
        resolucion.setIdOrdenVenta(String.valueOf(ordenVenta.getId()));
        resolucion.setCantidadVendida(cantidadTransaccion);
        resolucion.setDineroObtenido(montoTransaccion.longValue());
        ventaClient.procesarVenta(resolucion);
    }

    private void bloquearFondosComprador(Long usuarioId, Double monto) {
        try {
            SolicitudDineroDTO request = new SolicitudDineroDTO();
            request.setMonto(monto.longValue());
            Object respuesta = billeteraClient.bloquearFondos(request);
            if (respuesta == null) {
                throw new BloqueoFondosException(usuarioId);
            }
        } catch (BloqueoFondosException e) {
            throw e;
        } catch (Exception e) {
            throw new BloqueoFondosException(usuarioId);
        }
    }

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
