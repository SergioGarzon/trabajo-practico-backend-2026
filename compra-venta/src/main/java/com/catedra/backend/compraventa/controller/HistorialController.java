package com.catedra.backend.compraventa.controller;

import com.catedra.backend.compraventa.dto.TransaccionResponseDto;
import com.catedra.backend.compraventa.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
public class HistorialController {

    private final TransaccionService transaccionService;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TransaccionResponseDto>> misOperaciones(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(transaccionService.listarTransaccionesPorUsuario(usuarioId));
    }

    @GetMapping("/todas")
    public ResponseEntity<List<TransaccionResponseDto>> todasLasOperaciones() {
        return ResponseEntity.ok(transaccionService.listarTodasLasTransacciones());
    }
}
