package com.catedra.backend.compraventa.controller;

import com.catedra.backend.compraventa.dto.TransaccionResponseDto;
import com.catedra.backend.compraventa.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionResponseDto> obtenerTransaccionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transaccionService.obtenerTransaccionPorId(id));
    }

    @GetMapping("/compra/{ordenCompraId}")
    public ResponseEntity<List<TransaccionResponseDto>> listarPorOrdenCompra(@PathVariable Long ordenCompraId) {
        return ResponseEntity.ok(transaccionService.listarTransaccionesPorOrdenCompra(ordenCompraId));
    }

    @GetMapping("/venta/{ordenVentaId}")
    public ResponseEntity<List<TransaccionResponseDto>> listarPorOrdenVenta(@PathVariable Long ordenVentaId) {
        return ResponseEntity.ok(transaccionService.listarTransaccionesPorOrdenVenta(ordenVentaId));
    }
}
