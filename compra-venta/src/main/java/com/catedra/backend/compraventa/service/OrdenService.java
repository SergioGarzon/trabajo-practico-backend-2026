package com.catedra.backend.compraventa.service;

import com.catedra.backend.compraventa.dto.OrdenCompraRequestDto;
import com.catedra.backend.compraventa.dto.OrdenResponseDto;
import com.catedra.backend.compraventa.dto.OrdenVentaRequestDto;
import com.catedra.backend.compraventa.exception.OrdenNoEncontradaException;
import com.catedra.backend.compraventa.repository.OrdenCompraRepository;
import com.catedra.backend.compraventa.repository.OrdenVentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenVentaRepository ordenVentaRepository;

    public OrdenResponseDto crearOrdenCompra(OrdenCompraRequestDto requestDto) {
        return null;
    }

    public OrdenResponseDto crearOrdenVenta(OrdenVentaRequestDto requestDto) {
        return null;
    }

    public OrdenResponseDto obtenerOrdenCompraPorId(Long id) {
        ordenCompraRepository.findById(id)
                .orElseThrow(() -> new OrdenNoEncontradaException(id));
        return null;
    }

    public OrdenResponseDto obtenerOrdenVentaPorId(Long id) {
        ordenVentaRepository.findById(id)
                .orElseThrow(() -> new OrdenNoEncontradaException(id));
        return null;
    }

    public List<OrdenResponseDto> listarOrdenesCompraPorUsuario(Long usuarioId) {
        return List.of();
    }

    public List<OrdenResponseDto> listarOrdenesVentaPorUsuario(Long usuarioId) {
        return List.of();
    }

    public OrdenResponseDto cancelarOrden(Long id, String tipoOrden) {
        return null;
    }
}
