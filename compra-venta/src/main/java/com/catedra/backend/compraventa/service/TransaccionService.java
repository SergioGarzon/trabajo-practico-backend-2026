package com.catedra.backend.compraventa.service;

import com.catedra.backend.compraventa.dto.TransaccionResponseDto;
import com.catedra.backend.compraventa.models.Transaccion;
import com.catedra.backend.compraventa.exception.TransaccionNoEncontradaException;
import com.catedra.backend.compraventa.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;

    public TransaccionResponseDto obtenerTransaccionPorId(Long id) {
        Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(() -> new TransaccionNoEncontradaException(id));
        return mapearAResponse(transaccion);
    }

    public List<TransaccionResponseDto> listarTransaccionesPorOrdenCompra(Long ordenCompraId) {
        return transaccionRepository.findByOrdenCompraId(ordenCompraId)
                .stream()
                .map(this::mapearAResponse)
                .toList();
    }

    public List<TransaccionResponseDto> listarTransaccionesPorOrdenVenta(Long ordenVentaId) {
        return transaccionRepository.findByOrdenVentaId(ordenVentaId)
                .stream()
                .map(this::mapearAResponse)
                .toList();
    }

    public List<TransaccionResponseDto> listarTransaccionesPorUsuario(Long usuarioId) {
        return transaccionRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::mapearAResponse)
                .toList();
    }

    public List<TransaccionResponseDto> listarTodasLasTransacciones() {
        return transaccionRepository.findAllOrderByFechaDesc()
                .stream()
                .map(this::mapearAResponse)
                .toList();
    }

    private TransaccionResponseDto mapearAResponse(Transaccion transaccion) {
        TransaccionResponseDto dto = new TransaccionResponseDto();
        dto.setId(transaccion.getId());
        dto.setOrdenCompraId(transaccion.getOrdenCompra().getId());
        dto.setOrdenVentaId(transaccion.getOrdenVenta().getId());
        dto.setCantidadAcciones(transaccion.getCantidadAcciones());
        dto.setPrecioEjecucion(transaccion.getPrecioEjecucion());
        dto.setMontoTotal(transaccion.getMontoTotal());
        dto.setFecha(transaccion.getFecha());
        return dto;
    }
}
