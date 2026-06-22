package com.catedra.backend.compraventa.service;

import com.catedra.backend.compraventa.dto.TransaccionResponseDto;
import com.catedra.backend.compraventa.entity.OrdenCompra;
import com.catedra.backend.compraventa.entity.OrdenVenta;
import com.catedra.backend.compraventa.exception.TransaccionNoEncontradaException;
import com.catedra.backend.compraventa.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;

    public TransaccionResponseDto procesarTransaccion(OrdenCompra ordenCompra, OrdenVenta ordenVenta, Long cantidadAcciones) {
        return null;
    }

    public TransaccionResponseDto obtenerTransaccionPorId(Long id) {
        transaccionRepository.findById(id)
                .orElseThrow(() -> new TransaccionNoEncontradaException(id));
        return null;
    }

    public List<TransaccionResponseDto> listarTransaccionesPorOrdenCompra(Long ordenCompraId) {
        return List.of();
    }

    public List<TransaccionResponseDto> listarTransaccionesPorOrdenVenta(Long ordenVentaId) {
        return List.of();
    }
}
