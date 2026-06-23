package com.utnfrc.usuario_portfolios.controllers;

import com.utnfrc.usuario_portfolios.models.Accion;
import com.utnfrc.usuario_portfolios.services.AccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/acciones")
public class AccionController {

    @Autowired
    private AccionService accionService;

    @GetMapping("/simbolo/{simbolo}")
    public ResponseEntity<Accion> getBySimbolo(@PathVariable String simbolo) {
        Accion a = accionService.buscarPorSimbolo(simbolo);
        if (a == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(a);
    }

    @PostMapping("/")
    public Accion save() {
        return accionService.crear();
    }

    @GetMapping("/guardar")
    public ResponseEntity<Void> guardar() {accionService.sincronizarAcciones(); return ResponseEntity.ok().build();}

}
