package com.catedra.backend.compraventa.controller;

import com.catedra.backend.compraventa.dto.OrdenCompraRequestDto;
import com.catedra.backend.compraventa.dto.OrdenResponseDto;
import com.catedra.backend.compraventa.dto.OrdenVentaRequestDto;
import com.catedra.backend.compraventa.service.OrdenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;

    @PostMapping("/compra")
    public ResponseEntity<OrdenResponseDto> crearOrdenCompra(@Valid @RequestBody OrdenCompraRequestDto requestDto) {
        OrdenResponseDto response = ordenService.crearOrdenCompra(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/venta")
    public ResponseEntity<OrdenResponseDto> crearOrdenVenta(@Valid @RequestBody OrdenVentaRequestDto requestDto) {
        OrdenResponseDto response = ordenService.crearOrdenVenta(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/compra/{id}")
    public ResponseEntity<OrdenResponseDto> obtenerOrdenCompraPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.obtenerOrdenCompraPorId(id));
    }

    @GetMapping("/venta/{id}")
    public ResponseEntity<OrdenResponseDto> obtenerOrdenVentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.obtenerOrdenVentaPorId(id));
    }

    @GetMapping("/compra/usuario/{usuarioId}")
    public ResponseEntity<List<OrdenResponseDto>> listarOrdenesCompraPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ordenService.listarOrdenesCompraPorUsuario(usuarioId));
    }

    @GetMapping("/venta/usuario/{usuarioId}")
    public ResponseEntity<List<OrdenResponseDto>> listarOrdenesVentaPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ordenService.listarOrdenesVentaPorUsuario(usuarioId));
    }

    @PatchMapping("/compra/{id}/cancelar")
    public ResponseEntity<OrdenResponseDto> cancelarOrdenCompra(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.cancelarOrden(id, "COMPRA"));
    }

    @PatchMapping("/venta/{id}/cancelar")
    public ResponseEntity<OrdenResponseDto> cancelarOrdenVenta(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.cancelarOrden(id, "VENTA"));
    }
}
