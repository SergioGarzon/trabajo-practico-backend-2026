package com.catedra.backend.compraventa.service;

import com.catedra.backend.compraventa.dto.OrdenCompraRequestDto;
import com.catedra.backend.compraventa.dto.OrdenResponseDto;
import com.catedra.backend.compraventa.dto.OrdenVentaRequestDto;
import com.catedra.backend.compraventa.entity.OrdenCompra;
import com.catedra.backend.compraventa.entity.OrdenVenta;
import com.catedra.backend.compraventa.entity.enums.EstadoOrden;
import com.catedra.backend.compraventa.exception.OrdenNoEncontradaException;
import com.catedra.backend.compraventa.repository.OrdenCompraRepository;
import com.catedra.backend.compraventa.repository.OrdenVentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenVentaRepository ordenVentaRepository;
    private final EmparejamientoService emparejamientoService;

    @Transactional
    public OrdenResponseDto crearOrdenCompra(OrdenCompraRequestDto requestDto) {
        OrdenCompra ordenCompra = new OrdenCompra();
        ordenCompra.setUsuarioId(requestDto.getUsuarioId());
        ordenCompra.setSimboloAccion(requestDto.getSimboloAccion().toUpperCase());
        ordenCompra.setPrecioPorAccion(requestDto.getPrecioPorAccion());
        ordenCompra.setCantidadPedida(requestDto.getCantidadPedida());
        ordenCompra.setCantidadRestante(requestDto.getCantidadPedida());
        ordenCompra.setEstado(EstadoOrden.PENDIENTE);
        ordenCompra.setFechaCreacion(LocalDateTime.now());

        OrdenCompra ordenGuardada = ordenCompraRepository.save(ordenCompra);

        try {
            emparejamientoService.procesarOrdenCompra(ordenGuardada);
        } catch (Exception ignorada) {
        }

        return mapearOrdenCompraAResponse(ordenCompraRepository.findById(ordenGuardada.getId()).orElse(ordenGuardada));
    }

    @Transactional
    public OrdenResponseDto crearOrdenVenta(OrdenVentaRequestDto requestDto) {
        OrdenVenta ordenVenta = new OrdenVenta();
        ordenVenta.setUsuarioId(requestDto.getUsuarioId());
        ordenVenta.setSimboloAccion(requestDto.getSimboloAccion().toUpperCase());
        ordenVenta.setPrecioPorAccion(requestDto.getPrecioPorAccion());
        ordenVenta.setCantidadOriginal(requestDto.getCantidadOriginal());
        ordenVenta.setCantidadRestante(requestDto.getCantidadOriginal());
        ordenVenta.setEstado(EstadoOrden.PENDIENTE);
        ordenVenta.setFechaCreacion(LocalDateTime.now());

        OrdenVenta ordenGuardada = ordenVentaRepository.save(ordenVenta);

        try {
            emparejamientoService.procesarOrdenVenta(ordenGuardada);
        } catch (Exception ignorada) {
        }

        return mapearOrdenVentaAResponse(ordenVentaRepository.findById(ordenGuardada.getId()).orElse(ordenGuardada));
    }

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
