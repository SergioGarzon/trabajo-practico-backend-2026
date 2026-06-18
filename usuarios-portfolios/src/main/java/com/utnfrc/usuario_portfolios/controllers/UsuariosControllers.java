package com.utnfrc.usuario_portfolios.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.services.UsuariosServices;

@RestController
@RequestMapping("/usuarios")
public class UsuariosControllers {

    @Autowired
    private UsuariosServices service;

    @PostMapping
    public ResponseEntity<Usuarios> create(@RequestBody Usuarios usuario) {
        Usuarios created = service.create(usuario);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Usuarios>> all() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Usuarios> getById(@PathVariable Long dni) {
        return service.getById(dni)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{dni}")
    public ResponseEntity<Usuarios> update(@PathVariable Long dni, @RequestBody Usuarios usuario) {
        if (!service.getById(dni).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Usuarios updated = service.update(dni, usuario);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(@PathVariable Long dni) {
        if (!service.getById(dni).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(dni);
        return ResponseEntity.noContent().build();
    }
}
