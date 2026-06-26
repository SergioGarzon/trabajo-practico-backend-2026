package com.catedra.backend.compraventa.service;

import com.catedra.backend.compraventa.dto.OrdenCompraRequestDto;
import com.catedra.backend.compraventa.dto.OrdenResponseDto;
import com.catedra.backend.compraventa.dto.OrdenVentaRequestDto;
import com.catedra.backend.compraventa.exception.CotizacionNoEncontradaException;
import com.catedra.backend.compraventa.exception.MicroservicioFallaException;
import com.catedra.backend.compraventa.exception.OrdenNoEncontradaException;
import com.catedra.backend.compraventa.models.OrdenCompra;
import com.catedra.backend.compraventa.models.OrdenVenta;
import com.catedra.backend.compraventa.models.enums.EstadoOrden;
import com.catedra.backend.compraventa.repository.OrdenCompraRepository;
import com.catedra.backend.compraventa.repository.OrdenVentaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de gestión de órdenes de compra y venta.
 *
 * VALIDACIÓN DE SÍMBOLO (Tarea 4):
 * Antes de persistir cualquier orden nueva, se consulta el microservicio
 * servicio-yahoo-api-finance a través de CotizacionService para verificar
 * que el símbolo bursátil exista y sea operable.
 *
 * Si el símbolo no existe → CotizacionNoEncontradaException (HTTP 404).
 * Si la API externa falla → MicroservicioFallaException (HTTP 502).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrdenService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenVentaRepository ordenVentaRepository;
    private final EmparejamientoService emparejamientoService;

    // Inyectado como la implementación @Primary (CotizacionServiceImpl → Yahoo Finance)
    private final CotizacionService cotizacionService;

    // -------------------------------------------------------------------------
    // Creación de órdenes (con validación de símbolo previo a persistencia)
    // -------------------------------------------------------------------------

    @Transactional
    public OrdenResponseDto crearOrdenCompra(Long usuarioId, OrdenCompraRequestDto requestDto) {
        String simbolo = requestDto.getSimboloAccion().toUpperCase();

        // --- VALIDACIÓN TAREA 4: verificar símbolo contra servicio-yahoo-api-finance ---
        validarSimboloAccion(simbolo);

        OrdenCompra ordenCompra = new OrdenCompra();
        ordenCompra.setUsuarioId(usuarioId);
        ordenCompra.setSimboloAccion(simbolo);
        ordenCompra.setPrecioPorAccion(requestDto.getPrecioPorAccion());
        ordenCompra.setCantidadPedida(requestDto.getCantidadPedida());
        ordenCompra.setCantidadRestante(requestDto.getCantidadPedida());
        ordenCompra.setEstado(EstadoOrden.PENDIENTE);
        ordenCompra.setFechaCreacion(LocalDateTime.now());

        OrdenCompra ordenGuardada = ordenCompraRepository.save(ordenCompra);
        log.info("Orden de COMPRA #{} creada para usuario {} | símbolo={} | cantidad={} | precio={}",
                ordenGuardada.getId(), usuarioId, simbolo,
                requestDto.getCantidadPedida(), requestDto.getPrecioPorAccion());

        // Intento de emparejamiento inmediato (best-effort; si falla, la orden queda PENDIENTE)
        try {
            emparejamientoService.procesarOrdenCompra(ordenGuardada);
        } catch (Exception e) {
            log.warn("Emparejamiento no completado para orden de compra #{}: {}",
                    ordenGuardada.getId(), e.getMessage());
        }

        return mapearOrdenCompraAResponse(
                ordenCompraRepository.findById(ordenGuardada.getId()).orElse(ordenGuardada)
        );
    }

    @Transactional
    public OrdenResponseDto crearOrdenVenta(Long usuarioId, OrdenVentaRequestDto requestDto) {
        String simbolo = requestDto.getSimboloAccion().toUpperCase();

        // --- VALIDACIÓN TAREA 4: verificar símbolo contra servicio-yahoo-api-finance ---
        validarSimboloAccion(simbolo);

        OrdenVenta ordenVenta = new OrdenVenta();
        ordenVenta.setUsuarioId(usuarioId);
        ordenVenta.setSimboloAccion(simbolo);
        ordenVenta.setPrecioPorAccion(requestDto.getPrecioPorAccion());
        ordenVenta.setCantidadOriginal(requestDto.getCantidadOriginal());
        ordenVenta.setCantidadRestante(requestDto.getCantidadOriginal());
        ordenVenta.setEstado(EstadoOrden.PENDIENTE);
        ordenVenta.setFechaCreacion(LocalDateTime.now());

        OrdenVenta ordenGuardada = ordenVentaRepository.save(ordenVenta);
        log.info("Orden de VENTA #{} creada para usuario {} | símbolo={} | cantidad={} | precio={}",
                ordenGuardada.getId(), usuarioId, simbolo,
                requestDto.getCantidadOriginal(), requestDto.getPrecioPorAccion());

        // Intento de emparejamiento inmediato (best-effort; si falla, la orden queda PENDIENTE)
        try {
            emparejamientoService.procesarOrdenVenta(ordenGuardada);
        } catch (Exception e) {
            log.warn("Emparejamiento no completado para orden de venta #{}: {}",
                    ordenGuardada.getId(), e.getMessage());
        }

        return mapearOrdenVentaAResponse(
                ordenVentaRepository.findById(ordenGuardada.getId()).orElse(ordenGuardada)
        );
    }

    // -------------------------------------------------------------------------
    // Consulta de órdenes individuales
    // -------------------------------------------------------------------------

    public OrdenResponseDto obtenerOrdenCompraPorId(Long id) {
        OrdenCompra ordenCompra = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new OrdenNoEncontradaException(id));
        return mapearOrdenCompraAResponse(ordenCompra);
    }

    public OrdenResponseDto obtenerOrdenVentaPorId(Long id) {
        OrdenVenta ordenVenta = ordenVentaRepository.findById(id)
                .orElseThrow(() -> new OrdenNoEncontradaException(id));
        return mapearOrdenVentaAResponse(ordenVenta);
    }

    // -------------------------------------------------------------------------
    // Listados por usuario
    // -------------------------------------------------------------------------

    public List<OrdenResponseDto> listarOrdenesCompraPorUsuario(Long usuarioId) {
        return ordenCompraRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::mapearOrdenCompraAResponse)
                .toList();
    }

    public List<OrdenResponseDto> listarOrdenesVentaPorUsuario(Long usuarioId) {
        return ordenVentaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::mapearOrdenVentaAResponse)
                .toList();
    }

    // -------------------------------------------------------------------------
    // Cancelación de órdenes
    // -------------------------------------------------------------------------

    @Transactional
    public OrdenResponseDto cancelarOrden(Long id, String tipoOrden) {
        if ("COMPRA".equalsIgnoreCase(tipoOrden)) {
            OrdenCompra orden = ordenCompraRepository.findById(id)
                    .orElseThrow(() -> new OrdenNoEncontradaException(id));
            orden.setEstado(EstadoOrden.CANCELADA);
            orden.setFechaFinalizacion(LocalDateTime.now());
            ordenCompraRepository.save(orden);
            return mapearOrdenCompraAResponse(orden);
        }

        OrdenVenta orden = ordenVentaRepository.findById(id)
                .orElseThrow(() -> new OrdenNoEncontradaException(id));
        orden.setEstado(EstadoOrden.CANCELADA);
        orden.setFechaFinalizacion(LocalDateTime.now());
        ordenVentaRepository.save(orden);
        return mapearOrdenVentaAResponse(orden);
    }

    // -------------------------------------------------------------------------
    // VALIDACIÓN DE SÍMBOLO (Tarea 4)
    // -------------------------------------------------------------------------

    /**
     * Valida que el símbolo bursátil exista en Yahoo Finance antes de
     * persistir la orden.
     *
     * @param simbolo símbolo en mayúsculas (ej. "AAPL", "NVDA")
     * @throws CotizacionNoEncontradaException si el símbolo no existe en Yahoo Finance
     * @throws MicroservicioFallaException      si el microservicio de cotizaciones no responde
     */
    private void validarSimboloAccion(String simbolo) {
        log.debug("Validando símbolo '{}' contra servicio-yahoo-api-finance...", simbolo);
        try {
            boolean existe = cotizacionService.existeSimbolo(simbolo);
            if (!existe) {
                log.warn("Símbolo '{}' no encontrado en Yahoo Finance. Orden rechazada.", simbolo);
                throw new CotizacionNoEncontradaException(simbolo);
            }
            log.debug("Símbolo '{}' validado correctamente.", simbolo);
        } catch (CotizacionNoEncontradaException e) {
            // Re-lanzar tal cual para que GlobalExceptionHandler devuelva 404
            throw e;
        } catch (FeignException.ServiceUnavailable | FeignException.GatewayTimeout e) {
            log.error("El microservicio de cotizaciones no está disponible al validar '{}': {}",
                    simbolo, e.getMessage());
            throw new MicroservicioFallaException(
                    "servicio-yahoo-api-finance",
                    "El servicio de cotizaciones no está disponible. Intente nuevamente más tarde.");
        } catch (Exception e) {
            log.error("Error inesperado al validar símbolo '{}' contra Yahoo Finance: {}",
                    simbolo, e.getMessage());
            throw new MicroservicioFallaException(
                    "servicio-yahoo-api-finance",
                    "Error al comunicarse con el servicio de cotizaciones: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Mappers
    // -------------------------------------------------------------------------

    private OrdenResponseDto mapearOrdenCompraAResponse(OrdenCompra orden) {
        OrdenResponseDto dto = new OrdenResponseDto();
        dto.setId(orden.getId());
        dto.setUsuarioId(orden.getUsuarioId());
        dto.setSimboloAccion(orden.getSimboloAccion());
        dto.setPrecioPorAccion(orden.getPrecioPorAccion());
        dto.setEstado(orden.getEstado());
        dto.setFechaCreacion(orden.getFechaCreacion());
        dto.setFechaFinalizacion(orden.getFechaFinalizacion());
        dto.setTipoOrden("COMPRA");
        dto.setCantidad(orden.getCantidadPedida());
        return dto;
    }

    private OrdenResponseDto mapearOrdenVentaAResponse(OrdenVenta orden) {
        OrdenResponseDto dto = new OrdenResponseDto();
        dto.setId(orden.getId());
        dto.setUsuarioId(orden.getUsuarioId());
        dto.setSimboloAccion(orden.getSimboloAccion());
        dto.setPrecioPorAccion(orden.getPrecioPorAccion());
        dto.setEstado(orden.getEstado());
        dto.setFechaCreacion(orden.getFechaCreacion());
        dto.setFechaFinalizacion(orden.getFechaFinalizacion());
        dto.setTipoOrden("VENTA");
        dto.setCantidad(orden.getCantidadOriginal());
        return dto;
    }
}
