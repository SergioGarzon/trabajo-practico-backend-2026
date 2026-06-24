package com.catedra.backend.compraventa.service;

import com.catedra.backend.compraventa.client.BilleteraClient;
import com.catedra.backend.compraventa.client.PortfolioClient;
import com.catedra.backend.compraventa.dto.BilleteraOperacionRequestDto;
import com.catedra.backend.compraventa.dto.BilleteraOperacionResponseDto;
import com.catedra.backend.compraventa.dto.PortfolioOperacionRequestDto;
import com.catedra.backend.compraventa.entity.OrdenCompra;
import com.catedra.backend.compraventa.entity.OrdenVenta;
import com.catedra.backend.compraventa.entity.Transaccion;
import com.catedra.backend.compraventa.entity.enums.EstadoOrden;
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
    private final PortfolioClient portfolioClient;

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

        resolverFondosComprador(ordenCompra.getUsuarioId(), montoTransaccion);

        transferirAcciones(ordenVenta.getUsuarioId(), ordenCompra.getUsuarioId(),
                ordenCompra.getSimboloAccion(), cantidadTransaccion);
    }

    private void bloquearFondosComprador(Long usuarioId, Double monto) {
        try {
            BilleteraOperacionResponseDto respuesta = billeteraClient.bloquearFondos(
                    new BilleteraOperacionRequestDto(usuarioId, monto));
            if (respuesta == null || !Boolean.TRUE.equals(respuesta.getExitoso())) {
                throw new BloqueoFondosException(usuarioId);
            }
        } catch (BloqueoFondosException e) {
            throw e;
        } catch (Exception e) {
            throw new BloqueoFondosException(usuarioId);
        }
    }

    private void resolverFondosComprador(Long usuarioId, Double monto) {
        billeteraClient.resolverOperacion(new BilleteraOperacionRequestDto(usuarioId, monto));
    }

    private void transferirAcciones(Long vendedorId, Long compradorId, String simboloAccion, Long cantidad) {
        portfolioClient.actualizarTenencia(new PortfolioOperacionRequestDto(vendedorId, simboloAccion, -cantidad));
        portfolioClient.actualizarTenencia(new PortfolioOperacionRequestDto(compradorId, simboloAccion, cantidad));
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
