package com.utnfrc.usuario_portfolios.controllers;

import com.utnfrc.usuario_portfolios.models.RegistroOperacion;
import com.utnfrc.usuario_portfolios.services.RegistroOperacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/historial")
public class RegistroOperacionController {

    @Autowired
    private RegistroOperacionService registroOperacionService;

    @GetMapping
    public ResponseEntity<List<RegistroOperacion>> obtenerHistorial(@PathVariable Long usuarioId) {
        // Opcional: Aquí podrías validar que el usuario que consulta sea el dueño del ID (por seguridad)

        List<RegistroOperacion> historial = registroOperacionService.obtenerHistorialPorUsuario(usuarioId);
        return ResponseEntity.ok(historial);
    }
}