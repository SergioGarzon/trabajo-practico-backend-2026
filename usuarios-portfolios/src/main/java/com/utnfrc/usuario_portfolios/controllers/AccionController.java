package com.utnfrc.usuario_portfolios.controllers;

import com.utnfrc.usuario_portfolios.models.Accion;
import com.utnfrc.usuario_portfolios.services.AccionService;
import com.utnfrc.usuario_portfolios.services.IAccionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/acciones")
public class AccionController {

    @Autowired
    private IAccionService accionService;

    @GetMapping("/simbolo/{simbolo}")
    public ResponseEntity<Accion> getBySimbolo(@PathVariable String simbolo) {
        Accion a = accionService.buscarPorSimbolo(simbolo);
        if (a == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(a);
    }

    @GetMapping()
    public ResponseEntity<List<Accion>> buscarTodos() {
        return ResponseEntity.ok(accionService.buscarTodos());
    }

    // Esta mal esto
    @PostMapping("/")
    public Accion save() {
        return accionService.crear();
    }

    // No se usa nunca un GET para guardar datos
    @GetMapping("/guardar")
    public ResponseEntity<Void> guardar() {
        accionService.sincronizarAcciones(); 
        return ResponseEntity.ok().build();
    }

}
