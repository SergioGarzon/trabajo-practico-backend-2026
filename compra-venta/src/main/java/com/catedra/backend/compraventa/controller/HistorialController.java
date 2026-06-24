package com.catedra.backend.compraventa.controller;

import com.catedra.backend.compraventa.dto.TransaccionResponseDto;
import com.catedra.backend.compraventa.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
public class HistorialController {

    private final TransaccionService transaccionService;

    @GetMapping("/mis-operaciones")
    public ResponseEntity<List<TransaccionResponseDto>> misOperaciones(JwtAuthenticationToken jwt) {
        Long usuarioId = Long.valueOf(jwt.getToken().getSubject());
        return ResponseEntity.ok(transaccionService.listarTransaccionesPorUsuario(usuarioId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/todas")
    public ResponseEntity<List<TransaccionResponseDto>> todasLasOperaciones() {
        return ResponseEntity.ok(transaccionService.listarTodasLasTransacciones());
    }
}
