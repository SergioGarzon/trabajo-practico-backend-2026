package com.catedra.backend.compraventa.controller;

import com.catedra.backend.compraventa.dto.OrdenCompraRequestDto;
import com.catedra.backend.compraventa.dto.OrdenResponseDto;
import com.catedra.backend.compraventa.dto.OrdenVentaRequestDto;
import com.catedra.backend.compraventa.service.OrdenService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;

    @PostMapping("/compra")
    public ResponseEntity<OrdenResponseDto> crearOrdenCompra(JwtAuthenticationToken jwt,
                                                              @RequestBody OrdenCompraRequestDto requestDto) {
        String usuarioId = extraerUsuarioId(jwt);
        OrdenResponseDto response = ordenService.crearOrdenCompra(usuarioId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/venta")
    public ResponseEntity<OrdenResponseDto> crearOrdenVenta(JwtAuthenticationToken jwt,
                                                             @RequestBody OrdenVentaRequestDto requestDto) {
        String usuarioId = extraerUsuarioId(jwt);
        OrdenResponseDto response = ordenService.crearOrdenVenta(usuarioId, requestDto);
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

    private String extraerUsuarioId(JwtAuthenticationToken jwt) {
        return jwt.getToken().getSubject();
    }
}
